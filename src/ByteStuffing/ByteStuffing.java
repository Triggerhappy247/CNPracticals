package ByteStuffing;/*Byte Stuffing Implementation
Author 16bce059
Flag- 01111110
Escape- 10000001*/

import java.io.*;
import java.lang.Integer;
import java.util.Scanner;

class ByteStuffing
{
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Full FilePath: ");
		String filepath = scanner.nextLine();
        System.out.println("Full FileName: ");
        String filename = scanner.nextLine();
        System.out.println("1.Send\n2.Receive\nChoose Option:");
		int choice = scanner.nextInt();
		int flag = Integer.parseInt("01111110",2);
		int escape = Integer.parseInt("10000001",2);
		int fsize = 1000;
		System.out.println("Flag : " + (byte)flag);
		System.out.println("Escape : " + (byte)escape);
		System.out.println("SocketTest.Frame Size : " + fsize);
		switch(choice)
		{
			case 1: System.out.println("Framing...");
					Frame(filepath,filename,flag,escape,fsize);
					break;
			case 2: System.out.println("DeFraming...");
					DeFrame(filepath,filename,flag,escape,fsize);
					break;
		}
	}

	public static void Frame(String filepath,String filename,int flag,int escape,int fsize)
	{
		try
		{
			File in = new File(String.format("%s/%s",filepath,filename));
			File out = new File(String.format("%s.temp",String.format("%s/%s",filepath,filename)));
			FileInputStream fin = new FileInputStream(in);
			FileOutputStream fout = new FileOutputStream(out);
			System.out.println("Input File Size: " + in.length());
			int bite = fin.read();
			while(bite != -1)
			{
				fout.write(flag);
				for(int i = 0;i < fsize;i++)
				{
					//Byte Stuffing
					if(bite == flag || bite == escape)
						fout.write(escape);
					fout.write(bite);
					bite = fin.read();
					if(bite == -1)
						break;
				}
				fout.write(flag);
			}
			fin.close();
			fout.close();
			System.out.println("Output File Size: " + out.length());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void DeFrame(String filepath,String filename,int flag,int escape,int fsize)
	{
		try
		{
			File in = new File(String.format("%s.temp",String.format("%s/%s",filepath,filename)));
			File out = new File(String.format("%s/new%s",filepath,filename));
			FileInputStream fin = new FileInputStream(in);
			FileOutputStream fout = new FileOutputStream(out);
			System.out.println("Input File Size: " + in.length());
			//Read and ignore first flag
			fin.read();
			int bite = fin.read();
			while(bite != -1)
			{	
				if(bite == escape)
				{
					//Omit escape flags
					bite = fin.read();
				}
				fout.write(bite);
				bite = fin.read();
				if(bite == flag)
				{
					fin.read();
					bite = fin.read();
				}
			}
			fin.close();
			fout.close();
			System.out.println("Output File Size: " + out.length());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}