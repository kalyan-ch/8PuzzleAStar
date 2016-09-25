package edu.uncc.IS;
import java.util.*;
public class Puzzle8AStr {
	private int[][] goalState;
	private static int count;
	
	public static void main(String[] args) {
		
		//creating and declaring variables
		Scanner sc = new Scanner(System.in);
		Puzzle8AStr p = new Puzzle8AStr();
		p.goalState = new int[3][3];
		int[][] intState = new int[3][3];
		PriorityQueue<int[][]> pQ = new PriorityQueue<int[][]>(p.new HeuristicComp());
		List<int[][]> pathFlwd = new ArrayList<int[][]>();
		
		//taking input
		System.out.println("Enter the initial state: ");
		for(int i=0; i<3; i++){
			for(int j=0; j<3;j++){
				intState[i][j]=sc.nextInt();
			}
		}
		
		System.out.println("Enter the Goal state: ");
		for(int i=0; i<3; i++){
			for(int j=0; j<3;j++){
				p.goalState[i][j]=sc.nextInt();
			}
		}
		
		//adding the first node to priority queue
		pQ.add(intState);
		count= 0;
		
		//list to store all nodes ever generated
		List<int[][]> allNodes = new ArrayList<int[][]>();
		allNodes.add(intState);
		while(pQ.size() > 0){
			int[][] currSt = pQ.poll();
			pathFlwd.add(currSt);
			
			//comparing current and goalstate
			if(p.compareStates(currSt, p.goalState)){
				break;
			}
			
			//getting the next possible states
			List<int[][]> nextSts = p.shuffle(currSt);
			
			//verifying and removing the parent states
			for(int i=0;i<nextSts.size();i++){
				int[][] nextSt = nextSts.get(i);
				
				Iterator<int[][]> it = allNodes.iterator();
				int eqFlag = 1;
				while(it.hasNext()){
					int[][] curNode = it.next();
					
					if(p.compareStates(curNode, nextSt)){
						eqFlag = 0;
					}
				}
				
				if(eqFlag == 1){
					pQ.add(nextSt);
				}
				allNodes.add(nextSt);
			}
			count += 1;
		}
		
		System.out.println("Solved in: "+count+" steps");
		System.out.println("Priority Queue size: "+pQ.size());
		System.out.println("Nodes Generated: "+allNodes.size());
		System.out.println("Path followed: ");
		Iterator<int[][]> it = pathFlwd.iterator();
		
		//printing the solution path
		while(it.hasNext()){
			int[][] maff = it.next();
			for(int i=0;i<3;i++){
				for(int j=0;j<3;j++){
					System.out.print(""+maff[i][j]+" ");
				}
				System.out.println("");
			}
			System.out.println("");
		}
		
		sc.close();
	}
	
	public List<int[][]> shuffle(int[][] currSt){
		List<int[][]> nextSts = new ArrayList<int[][]>();
		
		//getting zero coord
		String[] zCrd = findZero(currSt).split(",");
		int zI = Integer.parseInt(zCrd[0]);
		int zJ = Integer.parseInt(zCrd[1]);
		
		//generating next states
		for(int i=0;i<4;i++){
			String[] newCrd = new String[2];
			
			if (i==0){
				newCrd = left(zI,zJ).split(",");
			}else if(i==1){
				newCrd = right(zI,zJ).split(",");
			}else if(i==2){
				newCrd = up(zI,zJ).split(",");
			}else if(i==3){
				newCrd = down(zI,zJ).split(",");
			}
			int newI = Integer.parseInt(newCrd[0]);
			int newJ = Integer.parseInt(newCrd[1]);
			int[][] tempArr = new int[3][3];
			
			for(int x=0; x<3; x++){
				for(int y=0; y<3;y++){
					tempArr[x][y] = currSt[x][y];
				}
			}
			
			if((newI != -1 && newI != 3) && (newJ != -1 && newJ != 3)){
				tempArr[zI][zJ] = tempArr[newI][newJ];
				tempArr[newI][newJ] = 0;
				nextSts.add(tempArr);
			}
		}
		
		return nextSts;
	}
	
	public String findZero(int[][] currSt){
		int zI=0, zJ= 0;
		for(int i=0; i<3; i++){
			for(int j=0; j<3;j++){
				if(currSt[i][j] == 0){
					zI = i;
					zJ = j;
					break;
				}
			}
		}
		return ""+zI+","+zJ;
	}
	
	public String left(int i, int j){
		return ""+i+","+(j-1);
	}
	public String right(int i, int j){
		return ""+i+","+(j+1);
	}
	public String up(int i, int j){
		return ""+(i-1)+","+(j);
	}
	public String down(int i, int j){
		return ""+(i+1)+","+(j);
	}
	
	public int getFN(int[][] state){
		return count+calHeuristics(state);
	}
	
	public int calHeuristics(int[][] state){
		int hN = 0;
		for(int i=0; i<3; i++){
			for(int j=0; j<3;j++){
				int tile = state[i][j];
				if(tile != 0){
					String[] coord = searchInGoal(tile).split(",");
					int gI = Integer.parseInt(coord[0]);
					int gJ = Integer.parseInt(coord[1]);
					
					hN += calDistance(i, j, gI, gJ);
				}
				
			}
		}
		return hN;
	}
	
	public String searchInGoal(int tile){
		String retString = "";
		for(int i=0; i<3; i++){
			for(int j=0; j<3;j++){
				if(tile == goalState[i][j]){
					retString = i+","+j;
					break;
				}
			}
		}
		return retString;
	}
	
	public int calDistance(int x1, int y1, int x2, int y2){
		int d=0;
		
		d = Math.abs(x2-x1) + Math.abs(y2-y1);
		
		return d;
	}

	public boolean compareStates(int[][] crSt, int[][] glSt){
		boolean status = true;
		for(int i=0; i <3;i++){
			for(int j=0;j<3;j++){
				if(crSt[i][j] != glSt[i][j]){
					status = false;
					break;
				}
			}
		}
		return status;
	}

	private class HeuristicComp implements Comparator<int[][]> {

		@Override
		public int compare(int[][] o1, int[][] o2) {
			int inti = getFN(o1)-getFN(o2);
			return inti;
		}

	}

}
