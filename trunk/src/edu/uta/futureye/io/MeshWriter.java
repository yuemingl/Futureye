package edu.uta.futureye.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.swing.filechooser.FileSystemView;

import edu.uta.futureye.algebra.Vector;
import edu.uta.futureye.core.Element;
import edu.uta.futureye.core.Mesh;
import edu.uta.futureye.core.Node;
import edu.uta.futureye.util.ElementList;
import edu.uta.futureye.util.NodeList;

public class MeshWriter {
	Mesh mesh = null;
	
	public MeshWriter(Mesh mesh) {
		this.mesh = mesh;
	}
	
	public void writeTechplot(String fileName, Vector u) {
		FileOutputStream out;
		try {
			File file = new File(fileName);
			out = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
			PrintWriter br = new PrintWriter(writer);
			
			NodeList nodes = mesh.getNodeList();
			ElementList elements = mesh.getElementList();
			int nNode = nodes.size();
			int nElement = elements.size();
			int nMaxNodes = 0;
			for(int i=1;i<=elements.size();i++) {
				if(elements.at(i).nodes.size() > nMaxNodes)
					nMaxNodes = elements.at(i).nodes.size();
			}
			
			if(nMaxNodes % 3 == 0) {
				br.println("VARIABLES=\"X\",\"Y\",\"U\"");
				
				if(nMaxNodes == 3)
					br.println(String.format("ZONE F=FEPOINT ET=TRIANGLE N=%d E=%d",nNode,nElement));
				else if(nMaxNodes == 6)
					br.println(String.format("ZONE F=FEPOINT ET=TRIANGLE N=%d E=%d",nNode,4*nElement));
					
				for(int i=1;i<=nNode;i++) {
					Node node = nodes.at(i);
					br.println(String.format("%f    %f    %f", 
							node.coord(1),
							node.coord(2),
							u.get(i)));			
				}
				for(int i=1;i<=nElement;i++) {
					Element e = elements.at(i);
					if(e.nodes.size() == 3) {
						br.println(String.format("%d    %d    %d", 
								e.nodes.at(1).globalIndex,
								e.nodes.at(2).globalIndex,
								e.nodes.at(3).globalIndex
								));
					} else if(e.nodes.size() == 6) {
						br.println(String.format("%d    %d    %d", 
								e.nodes.at(1).globalIndex,
								e.nodes.at(4).globalIndex,
								e.nodes.at(6).globalIndex
								));						
						br.println(String.format("%d    %d    %d", 
								e.nodes.at(2).globalIndex,
								e.nodes.at(5).globalIndex,
								e.nodes.at(4).globalIndex
								));	
						br.println(String.format("%d    %d    %d", 
								e.nodes.at(3).globalIndex,
								e.nodes.at(6).globalIndex,
								e.nodes.at(5).globalIndex
								));	
						br.println(String.format("%d    %d    %d", 
								e.nodes.at(4).globalIndex,
								e.nodes.at(5).globalIndex,
								e.nodes.at(6).globalIndex
								));	
					} else {
						System.out.println("Error: TRIANGLE nodes number="+e.nodes.size());
					}
					
				}
			} else if(nMaxNodes % 4 == 0) {
				br.println("VARIABLES=\"X\",\"Y\",\"U\"");
				
				if(nMaxNodes == 4)
					br.println(String.format("ZONE F=FEPOINT ET=QUADRILATERAL N=%d E=%d",nNode,nElement));
				else if(nMaxNodes == 8)
					br.println(String.format("ZONE F=FEPOINT ET=QUADRILATERAL N=%d E=%d",nNode,5*nElement));

				for(int i=1;i<=nNode;i++) {
					Node node = nodes.at(i);
					br.println(String.format("%f    %f    %f", 
							node.coord(1),
							node.coord(2),
							u.get(i)));			
				}
				for(int i=1;i<=nElement;i++) {
					Element e = elements.at(i);
					if(e.nodes.size() == 4) {
						br.println(String.format("%d    %d    %d    %d", 
								e.nodes.at(1).globalIndex,
								e.nodes.at(2).globalIndex,
								e.nodes.at(3).globalIndex,
								e.nodes.at(4).globalIndex
								));
					} else if(e.nodes.size() == 8) {
						br.println(String.format("%d    %d    %d    %d", 
								e.nodes.at(1).globalIndex,
								e.nodes.at(5).globalIndex,
								e.nodes.at(8).globalIndex,
								e.nodes.at(1).globalIndex
								));						
						br.println(String.format("%d    %d    %d    %d", 
								e.nodes.at(2).globalIndex,
								e.nodes.at(6).globalIndex,
								e.nodes.at(5).globalIndex,
								e.nodes.at(2).globalIndex
								));						
						br.println(String.format("%d    %d    %d    %d", 
								e.nodes.at(3).globalIndex,
								e.nodes.at(7).globalIndex,
								e.nodes.at(6).globalIndex,
								e.nodes.at(3).globalIndex
								));
						br.println(String.format("%d    %d    %d    %d", 
								e.nodes.at(4).globalIndex,
								e.nodes.at(8).globalIndex,
								e.nodes.at(7).globalIndex,
								e.nodes.at(4).globalIndex
								));
						br.println(String.format("%d    %d    %d    %d", 
								e.nodes.at(5).globalIndex,
								e.nodes.at(6).globalIndex,
								e.nodes.at(7).globalIndex,
								e.nodes.at(8).globalIndex
								));						
					} else if(e.nodes.size() == 3) {
						br.println(String.format("%d    %d    %d    %d", 
								e.nodes.at(1).globalIndex,
								e.nodes.at(2).globalIndex,
								e.nodes.at(3).globalIndex,
								e.nodes.at(1).globalIndex
								));
					} else {
						System.out.println("Error: QUADRILATERAL nodes number="+e.nodes.size());
					}
					
				}				
			}
			br.close();
			out.close();
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
