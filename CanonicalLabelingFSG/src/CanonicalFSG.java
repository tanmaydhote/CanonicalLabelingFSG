import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
@SuppressWarnings("unused")

public class CanonicalFSG
{
	static int Total = 0;
	public static void main(String[] args) throws IOException
	{
	//	System.out.println("Gen");
		String inputFile = "/home/tanmay/workspace/CanonicalLabelingFSG/data/test.txt";
		long t1 = System.currentTimeMillis();
		getGraphInfo(inputFile);
	//	CanonicalLabeling(inputFile);
		System.out.println(System.currentTimeMillis()-t1);
		System.out.println("Total = " + Total);
	}
	public static  void getGraphInfo(String inputFile) throws IOException
	{
		ArrayList<Graph> GraphList = new ArrayList<Graph>();
		BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
		String line = null;
		String[] items = null;
		Graph g = null;
		char c;
		int valueCount = 0,id=0,nodeCount=0,edgeCount=0;
		while ((line = br.readLine()) != null) {
			items = line.split("\\s+");
		//	System.out.println(items[0]);
			c = items[0].charAt(0);
			if(items.length==1 && !Character.isLetter(c) )
			{
				if(items[0].substring(0,1).equals("#"))
				{
					g = new Graph();
					valueCount = 1;
					id = Integer.parseInt(items[0].substring(1));
				//	System.out.println(id);
				}
				else
				{
					if(valueCount==1)
					{
						nodeCount = Integer.parseInt(items[0].substring(0));
				//		System.out.println(nodeCount);
					}
					else
					{
						edgeCount = Integer.parseInt(items[0].substring(0));
				//		System.out.println(edgeCount);
						g.initialise(id, nodeCount, edgeCount);
						GraphList.add(g);
					}
					valueCount++;
				}
			//	System.out.println("Value Count =" + valueCount);
			}
		}
		br.close();
		int maxSize = GraphList.get(0).NodeCount ,minSize = GraphList.get(0).NodeCount;
		for(int i =0 ;i<GraphList.size();i++)
		{
	//		System.out.println("ID = " + GraphList.get(i).ID + " Nodes = " + GraphList.get(i).NodeCount 
	//				+ " Edges = " + GraphList.get(i).EdgeCount );
			System.out.println(GraphList.get(i).ID + " " + GraphList.get(i).NodeCount);
			if(GraphList.get(i).NodeCount>maxSize)
			{
				maxSize = GraphList.get(i).NodeCount;
			}
			if(GraphList.get(i).NodeCount<minSize)
			{
				minSize = GraphList.get(i).NodeCount;
			}
		}
	//	System.out.println("Total number of graphs =" + GraphList.size());
	//	System.out.println("Max Size =" + maxSize + " Min Size ="+ minSize);
	}
	public static  void CanonicalLabeling(String inputFile) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
		String line = null;
		String[] items = null;
		Graph g = null;
		char c;
		int valueCount = 0,id=0,nodeCount=0,edgeCount=0;
		int[][] adjacencyList = null;
		ArrayList<Character> nodeLabels = new ArrayList<Character>();
		boolean first = true;
		while ((line = br.readLine()) != null) {
			items = line.split("\\s+");
			c = items[0].charAt(0);
			if(items.length==1 && !Character.isLetter(c) )
			{
				if(items[0].substring(0,1).equals("#"))
				{
					if(!first)
					{
				/*		System.out.println("ID =" + id );
						//findlabelsusingMatrixAndLabels
						for(int i = 0;i< nodeCount;i++)
						{
							for(int j = 0;j< nodeCount;j++)
							{
								System.out.print(adjacencyList[i][j] + " ");
							}
							System.out.println();
						}
						System.out.println(nodeLabels);
						System.out.println("Do stuff with matrix");*/
						DegreeLabel(adjacencyList, nodeLabels);
				//		NeighborLabel(adjacencyList, nodeLabels);
					}
					first = false;
					adjacencyList = null;
					nodeLabels.clear();
					valueCount = 1;
					id = Integer.parseInt(items[0].substring(1));
				//	System.out.println(id);
				}
				else
				{
					if(valueCount==1)
					{
						nodeCount = Integer.parseInt(items[0].substring(0));
						System.out.print(nodeCount + " ");
						adjacencyList = new int[nodeCount][nodeCount];
						for(int i = 0;i< nodeCount;i++)
						{
							for(int j = 0;j< nodeCount;j++)
							{
								adjacencyList[i][j] = 0;
							}
						}
					}
					else
					{
						edgeCount = Integer.parseInt(items[0].substring(0));
				//		System.out.println(edgeCount);
					}
					valueCount++;
				}
			}
			else if(items.length==1 && Character.isLetter(c) )
			{
				nodeLabels.add(c);
			}
			else
			{
				adjacencyList[Integer.parseInt(items[0])][Integer.parseInt(items[1])] 
						= Integer.parseInt(items[2]);
				adjacencyList[Integer.parseInt(items[1])][Integer.parseInt(items[0])] 
						= Integer.parseInt(items[2]);
			}
		}	
	}
	public static void DegreeLabel(int[][] AdjacencyList,ArrayList<Character> NodeLabels )
	{
	//	System.out.println("LabelingScheme" + AdjacencyList.length + " " + NodeLabels.size());
		int nodeCount = NodeLabels.size(),degree;
		ArrayList<Node> NodeList = new ArrayList<Node>();
		ArrayList<ArrayList<Node>> Partitions = new ArrayList<ArrayList<Node>>();
		Node n;
/*		for(int i = 0;i< nodeCount;i++)
		{
			for(int j = 0;j< nodeCount;j++)
			{
				System.out.print(AdjacencyList[i][j] + " ");
			}
			System.out.println();
		}*/
	//	System.out.println(NodeLabels);
		//Create Invariants
		for(int i=0;i<NodeLabels.size();i++)
		{
			n = new Node();
			degree = 0;
			for(int j=0;j<NodeLabels.size();j++)
			{
				if(AdjacencyList[i][j]!=0) degree++;
			}
			n.degree = degree;
			n.id = i;
			n.label = NodeLabels.get(i);
			NodeList.add(n);
		}
	/*	for(int i=0;i<NodeList.size();i++)
		{
			System.out.println(NodeList.get(i).id + " " + NodeList.get(i).label + " " + 
		NodeList.get(i).degree );
		}*/
		for(int i=0;i<NodeList.size();i++)
		{
			//System.out.println(NodeList.get(i).id + " " + NodeList.get(i).label + " " + NodeList.get(i).degree );
			ArrayList<Node> Partition = new ArrayList<Node>();
			Partition.add(NodeList.get(i));
			for(int j=i+1;j<NodeList.size();j++)
			{
				if((NodeList.get(i).label == NodeList.get(j).label) && 
						(NodeList.get(i).degree == NodeList.get(j).degree) )
				{
					Partition.add(NodeList.get(j));
					NodeList.remove(j);
				}
			}
			Partitions.add(Partition);
		}
		Total = Total + Partitions.size();
	/*	System.out.println("Number of partitions = " + Partitions.size());
		int total = 0;
		for(int i=0;i<Partitions.size();i++)
		{	
			System.out.println("Partition Size "+ Partitions.get(i).size() );
			total = total + Partitions.get(i).size();
			System.out.println("Elements in Partition");
			for(int j=0;j<Partitions.get(i).size();j++)
			{
				System.out.println("ID = " + Partitions.get(i).get(j).id);
			}
		}
		System.out.println("Total =" + total);*/
		GenerateLabels(Partitions, AdjacencyList);
	}
	public static void NeighborLabel(int[][] AdjacencyList,ArrayList<Character> NodeLabels )
	{
	//	System.out.println("LabelingScheme" + AdjacencyList.length + " " + NodeLabels.size());
		int nodeCount = NodeLabels.size(),degree;
		ArrayList<Node> NodeList = new ArrayList<Node>();
		ArrayList<ArrayList<Node>> Partitions = new ArrayList<ArrayList<Node>>();
		Node n;
	/*	for(int i = 0;i< nodeCount;i++)
		{
			for(int j = 0;j< nodeCount;j++)
			{
				System.out.print(AdjacencyList[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println(NodeLabels);*/
		//Create Invariants
		for(int i=0;i<NodeLabels.size();i++)
		{
			n = new Node();
			degree = 0;
			for(int j=0;j<NodeLabels.size();j++)
			{
				if(AdjacencyList[i][j]!=0) degree++;
			}
			n.degree = degree;
			n.id = i;
			n.label = NodeLabels.get(i);
			NodeList.add(n);
		}
		for(int i=0;i<NodeList.size();i++)
		{
		//	System.out.println(NodeList.get(i).id + " " + NodeList.get(i).label + " " + 
		//NodeList.get(i).degree );
			for(int j=0;j<NodeList.size();j++)
			{
				if(AdjacencyList[i][j]!=0) 
				{
					NeighboringNode n1 = new NeighboringNode();
					n1.edgeLabel = AdjacencyList[i][j];
					n1.label = NodeList.get(j).label;
					n1.degree = NodeList.get(j).degree;
					NodeList.get(i).NeighborList.add(n1);
				}
			}
		}
	/*	for(int i=0;i<NodeList.size();i++)
		{
			System.out.println(i);
			for(int j=0; j<NodeList.get(i).NeighborList.size();j++)
			{
				System.out.println(NodeList.get(i).NeighborList.get(j).edgeLabel + " " + 
						NodeList.get(i).NeighborList.get(j).degree + " " + 
						NodeList.get(i).NeighborList.get(j).label);
			}
		} */
		for(int i=0;i<NodeList.size();i++)
		{
			//System.out.println(NodeList.get(i).id + " " + NodeList.get(i).label + " " + NodeList.get(i).degree );
			ArrayList<Node> Partition = new ArrayList<Node>();
			Partition.add(NodeList.get(i));
			for(int j=i+1;j<NodeList.size();j++)
			{
				if(ListEqualityCheck(NodeList.get(i).NeighborList, NodeList.get(j).NeighborList) ) 
				{
					Partition.add(NodeList.get(j));
					NodeList.remove(j);
				}
			}
			Partitions.add(Partition);
		}
		Total = Total + Partitions.size();
	/*	System.out.println("Number of partitions = " + Partitions.size());
		int total = 0;
		for(int i=0;i<Partitions.size();i++)
		{	
			System.out.println("Partition Size "+ Partitions.get(i).size() );
			total = total + Partitions.get(i).size();
			System.out.println("Elements in Partition");
			for(int j=0;j<Partitions.get(i).size();j++)
			{
				System.out.println("ID = " + Partitions.get(i).get(j).id);
			}
		}
		System.out.println("Total =" + total);*/
		GenerateLabels(Partitions, AdjacencyList);
	}
	public static boolean ListEqualityCheck(ArrayList<NeighboringNode> NeighborList1,
		ArrayList<NeighboringNode> NeighborList2)	
	{
		boolean truthValue = true;
		if(NeighborList1.size()!=NeighborList2.size())
		return false;
		for(int i=0;i<NeighborList1.size();i++)
		{
			if(NeighborList1.get(i).edgeLabel==NeighborList2.get(i).edgeLabel && 
					NeighborList1.get(i).label==NeighborList2.get(i).label &&
					NeighborList1.get(i).degree==NeighborList2.get(i).degree)
			{
				
			}
			else truthValue = false;
		}
		return truthValue;
	}
	public static void GenerateLabels(ArrayList<ArrayList<Node>> Partitions, int[][] adjacencyMatrix)
	{
		int[] numberOfPermutations = new int[Partitions.size()];
		for(int i=0;i<Partitions.size();i++)
		{
			if(Partitions.get(i).size()>5) numberOfPermutations[i]=120;
			else numberOfPermutations[i] = fact(Partitions.get(i).size());
		}
		double Runs = 1;
		double Permutations = 1;
		BigInteger totalPermutations = new BigInteger("1");
		BigInteger partitionSize;
		for(int i=0; i<Partitions.size();i++)
		{
		//	partitionSize = new BigInteger(String.valueOf(numberOfPermutations[i]));
		//	totalPermutations = totalPermutations.multiply(partitionSize) ;
		//	totalPermutations1 = totalPermutations1 * numberOfPermutations[i];
			Runs = Runs + Math.log(numberOfPermutations[i]);
			Permutations = Permutations + Math.log(fact(Partitions.get(i).size()));
		}
	//	System.out.println("Total number of permutations " + totalPermutations);
	//	System.out.println(totalPermutations + "  " + totalPermutations1);
		System.out.println(Permutations);	
		
	}
	
	public static int fact(int n)
    {
        int result;
       if(n==0 || n==1)
         return 1;

       result = fact(n-1) * n;
       return result;
    }
}