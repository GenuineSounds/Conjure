package com.genuinevm.test;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.genuinevm.data.collection.DataArray;
import com.genuinevm.data.collection.DataCompound;
import com.genuinevm.data.primitive.DataBoolean;

public class BytecodeOutput {

	private static final char[] HEX = "0123456789ABCDEF".toCharArray();

	public static void main(final String[] args) {
		final DataCompound main = new DataCompound();
		final DataCompound comp = new DataCompound();
		comp.set("name", "Poopster");
		comp.set("floaty", 1.0F);
		final DataArray array = new DataArray();
		array.add(DataBoolean.FALSE);
		array.add(DataBoolean.TRUE);
		array.add(DataBoolean.TRUE);
		array.add(DataBoolean.FALSE);
		array.add(DataBoolean.FALSE);
		array.add(DataBoolean.TRUE);
		main.set("BigDecimal", new BigDecimal("3.14159265358979"));
		main.set("BigInteger", new BigInteger("2345984321431024"));
		main.set("boolean", true);
		main.set("byte", (byte) 36);
		main.set("byte[]", new byte[] {
				(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE
		});
		main.set("DataArray", array);
		main.set("DataCompound", comp);
		main.set("float", 8.5F);
		main.set("double", 1234.5678);
		main.set("int[]", new int[] {
				0, 0x6E745B5D, 4, 89758,
		});
		main.set("int", 28941321);
		main.set("long", 0xCCCCCCCCCCCCCCCCL);
		main.set("short", Short.MAX_VALUE);
		main.set("String", "Test \"stuff\"");
		System.out.println(main);
	}

	public static String bytesToHex(final byte[] bytes) {
		final char[] hexChars = new char[bytes.length * 2];
		for (int i = 0; i < bytes.length; i++) {
			final int v = bytes[i] & 0xFF;
			hexChars[i * 2] = BytecodeOutput.HEX[v >>> 4];
			hexChars[i * 2 + 1] = BytecodeOutput.HEX[v & 0x0F];
		}
		return new String(hexChars);
	}
}
