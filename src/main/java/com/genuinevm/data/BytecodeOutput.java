package com.genuinevm.data;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.genuinevm.data.collection.DataArray;
import com.genuinevm.data.collection.DataCompound;
import com.genuinevm.data.primitive.DataBoolean;
import com.genuinevm.data.util.InputOutput;
import com.genuinevm.data.util.JSON;

public class BytecodeOutput {

	private static final char[] HEX = "0123456789ABCDEF".toCharArray();

	public static void main(String[] args) {
		final DataCompound MAIN = new DataCompound();
		DataCompound comp = new DataCompound();
		comp.set("name", "Poopster");
		comp.set("floaty", 1.0F);
		DataArray array = new DataArray();
		array.add(DataBoolean.FALSE);
		array.add(DataBoolean.TRUE);
		array.add(DataBoolean.TRUE);
		array.add(DataBoolean.FALSE);
		array.add(DataBoolean.FALSE);
		array.add(DataBoolean.TRUE);
		MAIN.set("BigDecimal", new BigDecimal("3.14159265358979323846264338327950288419716939937510582"));
		MAIN.set("BigInteger", new BigInteger("2345984321896432168945165165169849913216843216897431024"));
		MAIN.set("boolean", true);
		MAIN.set("byte", (byte) 36);
		MAIN.set("byte[]", new byte[] { (byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE });
		MAIN.set("DataCompound", comp);
		MAIN.set("float", 8.5F);
		MAIN.set("double", 1234.5678);
		MAIN.set("int[]", new int[] { 0x6E745B5D, 4, 0, 89758, });
		MAIN.set("int", 28941321);
		MAIN.set("long", 0xCCCCCCCCCCCCCCCCL);
		MAIN.set("short", Short.MAX_VALUE);
		MAIN.set("String", "Test \"stuff\"");
		try {
			System.out.println(bytesToHex(InputOutput.getBytes(MAIN)));
		}
		catch (IOException e) {}
	}

	public static String bytesToHex(final byte[] bytes) {
		final char[] hexChars = new char[bytes.length * 2];
		for (int i = 0; i < bytes.length; i++) {
			final int v = bytes[i] & 0xFF;
			hexChars[i * 2] = HEX[v >>> 4];
			hexChars[i * 2 + 1] = HEX[v & 0x0F];
		}
		return new String(hexChars);
	}
}
