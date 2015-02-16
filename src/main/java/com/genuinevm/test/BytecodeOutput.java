package com.genuinevm.test;

import java.io.IOException;

import com.genuinevm.data.collection.DataCompound;
import com.genuinevm.data.util.InputOutput;
import com.genuinevm.data.util.JSON;

public class BytecodeOutput {

	public static void main(final String[] args) {
		DataCompound main = new DataCompound();
		main.set("a", "9");
		System.out.println(JSON.toJSON(main));
		final String main1 = InputOutput.bytesToHex(InputOutput.getBytes(main));
		try {
			main = InputOutput.read(InputOutput.getBytes(JSON.thereAndBack(main)));
		}
		catch (final IOException e) {}
		System.out.println(JSON.toJSON(main));
		System.out.println("a: " + main.getString("a"));
		System.out.println(main1);
		System.out.println(InputOutput.bytesToHex(InputOutput.getBytes(main)));
	}
}
