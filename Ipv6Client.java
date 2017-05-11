import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class Ipv6Client {

	public static void main(String[] args) throws UnknownHostException {
		try (Socket socket = new Socket("codebank.xyz", 38004)){

			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);

			OutputStream os = socket.getOutputStream();
			PrintStream ps = new PrintStream(os);

			int size = 1;
			for (int i = 0; i < 1; ++i){
				size <<= 1;
				System.out.println("Data length: " + size);
				ps.write(getPackets(size));
//				System.out.println(br.readLine() + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static byte[] getPackets(int size){
		byte[] packets = new byte[40+size];
		packets[0] = (byte) ((packets[0] | 7) << 4); // version/traff/flow

		packets[4] = (byte) (size << 8);			// payload- len of only data in bytes
		packets[5] = (byte) (size & 0xFF);
		packets[6] = 0x11; 	//next header
		packets[7] = 20;	//hop limit
		
		packets[8] = (byte) 0xfe;
		packets[9] = (byte) 0x80;
		packets[10] = (byte) 0xe5;
		packets[11] = (byte) 0x4a;
		packets[12] = (byte) 0x85;
		packets[13] = (byte) 0x0e;
		packets[14] = (byte) 0x33;
		packets[15] = (byte) 0x77;
		packets[16] = (byte) 0x64;
		packets[17] = (byte) 0x87;
		
		packets[23] = (byte) 0xff;
		packets[24] = (byte) 0xff;
		packets[25] = (byte) 0x36;
		packets[26] = (byte) 0x25;
		packets[27] = (byte) 0x58;
		packets[28] = (byte) 0x9a;
		
		return packets;
	}
	public static void serverCode(Socket socket, BufferedReader br){
		byte[] response = new byte[4];
		for (int i = 0; i < 4; ++i){
			response[i] = br.read();
		}
	}
}
