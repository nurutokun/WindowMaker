package com.rawad.windowmaker.swing;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;

public class ChangeManager {
	
	private Stack<Change> changeList;
	private Stack<Change> undoneChanges;
	
	private Change currentChange;
	private Change currentUndoneChange;
	
	private boolean undoing;
	private boolean redoing;
	
	public ChangeManager() {
		
		changeList = new Stack<Change>();
		undoneChanges = new Stack<Change>();
		
		currentChange = new Change();
		currentUndoneChange = new Change();
		
		undoing = false;
		redoing = false;
		
	}
	
	public void changePixel(int x, int y, int oldColor) {
		
		if(undoing) {
			currentUndoneChange.addEdit(x, y, oldColor);
			
		} else if(redoing) {// Could merge these last two
			currentChange.addEdit(x, y, oldColor);
			
		} else {
			currentChange.addEdit(x, y, oldColor);
			
		}
		
	}
	
	public void changeDimensions(int oldWidth, int oldHeight) {
		
		if(undoing) {
			currentUndoneChange.addEdit(oldWidth, oldHeight);
			
		} else if(redoing) {
			currentChange.addEdit(oldWidth, oldHeight);
			
		} else {
			currentChange.addEdit(oldWidth, oldHeight);
			
		}
		
	}
	
	public void stopRecordingUndoChange() {
		
		try {
			
			if(currentChange.singleEdits.peek().equals(new Integer[]{0,0,0})) {
				return;
			}
			
		} catch(Exception ex) {
			return;
		}
		
		changeList.add(currentChange);
		currentChange = new Change();
	}
	
	public void stopRecordingRedoChange() {
		
		undoneChanges.push(currentUndoneChange);
		currentUndoneChange = new Change();
		
	}
	
	public void undoChange(CustomPanel drawingCanvas) {
		
		undoing = true;
		
		try {
			Change change = changeList.pop();
			
			change.undoOrRedoChange(drawingCanvas);
			
			stopRecordingRedoChange();
			
		} catch(EmptyStackException ex) {
//			System.out.println("Undo change not found...");
		}
		
		undoing = false;
		
	}
	
	public void redoChange(CustomPanel drawingCanvas) {
		
		redoing = true;
		
		try {
			Change change = undoneChanges.pop();
			
			change.undoOrRedoChange(drawingCanvas);
			
			stopRecordingUndoChange();
			
		} catch(EmptyStackException ex) {
//			System.out.println("Redo change not found...");
		}
		
		redoing = false;
		
	}
	
	public boolean isUndoing() {
		return undoing;
	}
	
	public boolean isRedoing() {
		return redoing;
	}
	
	private class Change {
		
		private LinkedList<Integer[]> singleEdits;
		
		public Change() {
			singleEdits = new LinkedList<Integer[]>();
		}
		
		public void undoOrRedoChange(CustomPanel drawingCanvas) {
			
			// Go through all pixels and change them AND/OR change the size of the image
			
			LinkedList<Integer[]> temp = singleEdits;
			
			int size = temp.size();
			
			for(int i = 0; i < size; i++) {
				Integer[] currentEdit = temp.remove();
				
//				System.out.println("Redoing? " + redoing + " i: " + i);
				
				handleEditUndoOrRedo(drawingCanvas, currentEdit);
				
			}
			
		}
		
		private void handleEditUndoOrRedo(CustomPanel drawingCanvas, Integer[] currentEdit) {
			
			ChangeType id = ChangeType.getChangeTypeById(currentEdit[0]);
			
			switch(id) {
			case PIXEL:
				
				int x = currentEdit[1];
				int y = currentEdit[2];
				
				int color = currentEdit[3];
				
				drawingCanvas.setPixel(x, y, color);
//				System.out.printf("changed pixel: %s, %s to color: %s\n", x, y, color);
				break;
			
			case DIMENSION:
				
				int width = currentEdit[1];
				int height = currentEdit[2];
				
				drawingCanvas.setNewImageDimensions(width, height);
				break;
				
			default:
				System.out.println("Missing ChangeType; needs to be handled...");
				break;
			
			}
			
		}
		
		public void addEdit(int x, int y, int color) {
			
			Integer[] edit = new Integer[]{ChangeType.PIXEL.getId(), x, y, color};
			Integer[] previousEdit = singleEdits.peek();
			
//			System.out.printf("x,y: %s, %s color: %s \n", x, y, color);
			
			boolean addEdit = true;
			
			int prevX = 0;
			int prevY = 0;
			int prevColor = 0;
			
			if(previousEdit != null) {
				
				prevX = previousEdit[1];
				prevY = previousEdit[2];
				prevColor = previousEdit[3];
				
				if(prevX == x && prevY == y && prevColor == color) {
					addEdit = false;
				}
				
			}
			
			if(addEdit) {
				singleEdits.add(edit);
			}
			
//			System.out.println(singleEdits.size() + " - " + previousEdit + " - x, y: " + prevX + ", " + prevY + " color: " + prevColor + 
//									" newX, newY: " + x + ", " + y + " newColor: " + color);
			
		}
		
		public void addEdit(int width, int height) {
			
			Integer[] edit = new Integer[]{ChangeType.DIMENSION.getId(), width, height};
			Integer[] previousEdit = singleEdits.peek();
			
			boolean addEdit = true;
			
			int prevWidth = 0;
			int prevHeight = 0;
			
			if(previousEdit != null ) {
				
				prevWidth = previousEdit[1];
				prevHeight = previousEdit[2];
				
				if(prevWidth == width && prevHeight == height) {
					addEdit = false;
				}
				
			}
			
			if(addEdit) {
				singleEdits.push(edit);
			}
			
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
