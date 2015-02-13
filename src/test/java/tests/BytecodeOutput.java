package tests;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.genuinevm.data.collection.DataCompound;
import com.genuinevm.data.util.InputOutput;
import com.genuinevm.data.util.JSON;

public class BytecodeOutput {

	private static final char[] HEX = "0123456789ABCDEF".toCharArray();
	private static final DataCompound MAIN = new DataCompound();

	public void displayBytes() throws IOException {
		System.out.println(bytesToHex(InputOutput.getBytes(MAIN)));
	}

	public static String bytesToHex(final byte[] bytes) {
		final char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			final int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX[v >>> 4];
			hexChars[j * 2 + 1] = HEX[v & 0x0F];
		}
		return new String(hexChars);
	}

	static {
		MAIN.set("BigDecimal", new BigDecimal("3.14159265358979323846264338327950288419716939937510582"));
		MAIN.set("BigInteger", new BigInteger("2345984321896432168945165165169849913216843216897431024"));
		MAIN.set("boolean", true);
		MAIN.set("byte", (byte) 36);
		MAIN.set("byte[]", new byte[] { (byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE });
		MAIN.set("DataCompound", new DataCompound());
		MAIN.set("float", 8.5F);
		MAIN.set("double", 1234.5678);
		MAIN.set("int[]", new int[] { 0x6E745B5D, 4, 0, 89758, });
		MAIN.set("int", 28941321);
		MAIN.set("long", 0xCCCCCCCCCCCCCCCCL);
		MAIN.set("short", Short.MAX_VALUE);
		MAIN.set("String", "Test \"stuff\"");
	}
}
