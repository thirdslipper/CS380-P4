/**
 * Author: Colin Koo
 * Professor: Nima Davarpanah
 * Description: Ipv6 Packet format implementation.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class Ipv6Client {
	/**
	 * Sends Ipv6 packets in sizes of 2-4096 bytes- powers of 2 to the server.
	 * @param args
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException {
		try (Socket socket = new Socket("codebank.xyz", 38004)){

			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			PrintStream ps = new PrintStream(os);

			short size = 1;
			for (int i = 0; i < 12; ++i){
				size <<= 1;
				System.out.println("Data length: " + size);
				ps.write(getPackets(size));
				serverCode(is);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Ipv6 formatting, where 0's in the packets are intentionally not declared because of the array
	 * initialization default values being 0.
	 * These fields include Version, Traffic Class, Flow Label, Payload length, Next Header, Hop Limit, 
	 * Source Address, and Destination Address, where the Traffic Class and Flow Label fields
	 * are intentionally not implemented.
	 * @param size
	 * @return
	 */
	public static byte[] getPackets(short size){
		short length = (short) (40+size);
		byte[] packets = new byte[length];

		packets[0] = 6;
		packets[0] <<= 4;
		//packet bytes 1-3 are 0's representing nonimplemented traffic class/flow label.

		packets[4] = (byte) ((size >> 8) & 0xFF);			// payload- len of only data in bytes
		packets[5] = (byte) (size & 0xFF);					
		packets[6] = 0x11; 	//next header, UDP = 0x11
		packets[7] = 20;	//hop limit
		
		//8-19 9's, 96 bits
		packets[18] = (byte) 0xFF;
		packets[19] = (byte) 0xFF;
		packets[20] = (byte) 76; 	//Src inet address: 0:0:0:0:0:FFFF:4CAF:55AE
		packets[21] = (byte) 175;	//255.255.76.175.85.174
		packets[22] = (byte) 85;	
		packets[23] = (byte) 174;
		
		packets[34] = (byte) 0xFF;
		packets[35] = (byte) 0xFF;
		packets[36] = (byte) 52;	//Dest socket inet address : 52.37.88.154
		packets[37] = (byte) 37;	//255.255.52.37.88.15
		packets[38] = (byte) 88;
		packets[39] = (byte) 154;
		
		return packets;
	}
	/**
	 * This method prints the message received from the server after each sent Ipv6 packet.
	 * @param is
	 */
	public static void serverCode(InputStream is){
		System.out.print("Response: 0x");
		for (int i = 0; i < 4; ++i){
			try {
				System.out.print(Integer.toHexString(is.read()).toUpperCase());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println();
	}
}
