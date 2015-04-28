package com.rawad.windowmaker.swing;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.LinkedList;
import java.util.Queue;

public class ChangeManager {
	
	private Queue<Change> changeList;
	private Queue<Change> undoneChanges;
	
	private Change currentChange;
	
	private boolean reverting;
	
	public ChangeManager() {
		
		changeList = new LinkedList<Change>();
		undoneChanges = new LinkedList<Change>();
		
		currentChange = new Change();
		
		reverting = false;
		
	}
	
	public void changePixel(int x, int y, int oldColor) {
		currentChange.addEdit(x, y, oldColor);
	}
	
	public void changeDimensions(int oldWidth, int oldHeight) {
		currentChange.addEdit(oldWidth, oldHeight);
	}
	
	public void stopRecordingChange() {
		changeList.add(currentChange);
		
		currentChange = new Change();
	}
	
	public void undoChange(CustomPanel drawingCanvas) {
		
		reverting = true;
		
		try {
			Change change = changeList.remove();
			
			change.undoChange(drawingCanvas);
			
			undoneChanges.add(change);
		} catch(NoSuchElementException ex) {
			System.out.println("Change not found...");
		}
		
		reverting = false;
		
	}
	
	public boolean isReverting() {
		return reverting;
	}
	
	private class Change {
		
		private LinkedList<Integer[]> singleEdits;
		
		public Change() {
			singleEdits = new LinkedList<Integer[]>();
		}
		
		public void undoChange(CustomPanel drawingCanvas) {
			
			// Go through all pixels and change them AND/OR change the size of the image
			
			LinkedList<Integer[]> temp = singleEdits;
			
			for(int i = 0; i < temp.size(); i++) {
				Integer[] currentEdit = temp.remove();
				
				handleEdit(drawingCanvas, currentEdit);
				
			}
			
		}
		
		private void handleEdit(CustomPanel drawingCanvas, Integer[] currentEdit) {
			
			ChangeType id = ChangeType.getChangeTypeById(currentEdit[0]);
			
			switch(id) {
			case PIXEL:
				
				int x = currentEdit[1];
				int y = currentEdit[2];
				
				int color = currentEdit[3];
				
				drawingCanvas.setPixel(x, y, color ^ 0xFFFFFF);
				drawingCanvas.rescaleImage(drawingCanvas.getScaleFactor());
//				System.out.printf("changed pixel: %s, %s to color: %s\n", x, y, color);
				break;
			
			case DIMENSION:
				drawingCanvas.setNewImageDimensions(currentEdit[1], currentEdit[2]);
				break;
				
			default:
				System.out.println("Missing ChangeType; needs to be handled...");
				break;
			
			}
			
		}
		
		public void addEdit(int x, int y, int color) {
			Integer[] edit = new Integer[]{ChangeType.PIXEL.getId(), x, y, color};
			
			if(edit != singleEdits.peek()) {
				singleEdits.add(edit);
			}
			
		}
		
		public void addEdit(int width, int height) {
			singleEdits.add(new Integer[]{ChangeType.DIMENSION.getId(), width, height});
		}
		
	}
	
	private enum ChangeType {
		
		PIXEL(0),
		DIMENSION(1);
		
		private final int id;
		
		private ChangeType(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}
		
		public static ChangeType getChangeTypeById(int id) {
			ChangeType[] types = values();
			
			for(ChangeType ct: types) {
				
				if(ct.getId() == id) {
					return ct;
				}
				
			}
			
			return null;
			
		}
		
	}
	
}
