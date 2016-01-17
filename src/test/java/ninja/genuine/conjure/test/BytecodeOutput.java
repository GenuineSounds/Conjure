package ninja.genuine.conjure.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import ninja.genuine.conjure.data.collection.DataCompound;
import ninja.genuine.conjure.util.InputOutput;
import ninja.genuine.conjure.util.JSON;

public class BytecodeOutput {

	public static void main(final String[] args) {
		DataCompound main1 = new DataCompound();
		main1.set("a", "1");
		main1.set("b", 1);
		main1.set("b", 1L);
		main1.set("c", 1.0D);
		main1.set("d", 1.0F);
		main1.set("e", new BigInteger("1"));
		main1.set("f", BigDecimal.valueOf(1));
		try {
			String jMain1 = JSON.toJSON(main1);
			String hMain1 = InputOutput.bytesToHex(InputOutput.getBytes(main1));
			DataCompound main2 = InputOutput.read(InputOutput.getBytes(JSON.thereAndBack(main1)));
			String jMain2 = JSON.toJSON(main2);
			String hMain2 = InputOutput.bytesToHex(InputOutput.getBytes(main2));
			System.out.println(jMain1);
			System.out.println(jMain2);
			System.out.println(hMain1);
			System.out.println(hMain2);
		} catch (final IOException e) {}
	}

	public static String format(String input) {
		input = input.replaceAll("[\r\n\t]+", " ");
		input = input.replaceAll(",+([\\]\\}])", "$1");
		int tabs = 0;
		boolean quoted = false;
		StringBuilder builder = new StringBuilder(input);
		for (int index = 0; index < builder.length(); index++) {
			char c = builder.charAt(index);
			if (quoted && c != '"')
				continue;
			switch (c) {
				case '"':
					quoted = !(quoted && builder.charAt(index - 1) != '\\');
					continue;
				case ':':
					builder.insert(++index, ' ');
					continue;
				case '{':
				case '[':
					tabs++;
				case ',':
					builder.insert(index + 1, '\n');
					continue;
				case '}':
				case ']':
					tabs--;
					builder.insert(index++, '\n');
					for (int t = 0; t < tabs; t++)
						builder.insert(index++, '\t');
					continue;
				case '\n':
					for (int t = 0; t < tabs; t++)
						builder.insert(++index, '\t');
					continue;
			}
		}
		return builder.toString();
	}
}
