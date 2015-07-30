package com.genuinevm.conjure.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.genuinevm.conjure.TypeSystem;
import com.genuinevm.conjure.data.Data;
import com.genuinevm.conjure.data.collection.DataCompound;
import com.genuinevm.conjure.data.primitive.DataNull;

public class InputOutput {

	public static DataCompound open(final File file) throws IOException {
		if (!file.exists())
			return null;
		final DataInputStream input = new DataInputStream(new FileInputStream(file));
		final DataCompound compound = InputOutput.getDataCompound(input);
		input.close();
		return compound;
	}

	public static void save(final DataCompound compound, final File file) throws IOException {
		final DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));
		InputOutput.writeToOutput(compound, stream);
		stream.close();
	}

	public static void safeSave(final DataCompound compound, final File file) {
		try {
			final File tmp = new File(file.getAbsolutePath() + "_tmp");
			if (tmp.exists())
				tmp.delete();
			InputOutput.save(compound, tmp);
			if (file.exists())
				file.delete();
			if (file.exists())
				throw new IOException("Failed to delete " + file);
			else
				tmp.renameTo(file);
		}
		catch (final Exception e) {}
	}

	public static DataCompound read(final InputStream stream) throws IOException {
		final DataInputStream compressed = new DataInputStream(new BufferedInputStream(stream));
		final DataCompound data = InputOutput.getDataCompound(compressed);
		compressed.close();
		return data;
	}

	public static DataCompound read(final byte[] bytes) throws IOException {
		final DataInputStream compressed = new DataInputStream(
				new BufferedInputStream(new ByteArrayInputStream(bytes)));
		final DataCompound compound = InputOutput.getDataCompound(compressed);
		compressed.close();
		return compound;
	}

	public static DataCompound readCompressed(final InputStream stream) throws IOException {
		final DataInputStream compressed = new DataInputStream(new BufferedInputStream(new GZIPInputStream(stream)));
		final DataCompound data = InputOutput.getDataCompound(compressed);
		compressed.close();
		return data;
	}

	public static DataCompound readCompressed(final byte[] bytes) throws IOException {
		final DataInputStream compressed = new DataInputStream(
				new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes))));
		final DataCompound compound = InputOutput.getDataCompound(compressed);
		compressed.close();
		return compound;
	}

	public static DataCompound getDataCompound(final DataInput input) throws IOException {
		final Data<?> data = InputOutput.getData(input);
		if (data instanceof DataCompound)
			return (DataCompound) data;
		throw new IOException("DataCompound was not the containing data type");
	}

	private static Data<?> getData(final DataInput input) throws IOException {
		final byte type = input.readByte();
		if (type == 0)
			return DataNull.INSTANCE;
		input.readUTF();
		final Data<?> data = TypeSystem.getTypeSystem().createByCode(type);
		data.read(input);
		return data;
	}

	public static byte[] getBytes(final DataCompound compound) {
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		final DataOutputStream output = new DataOutputStream(bytes);
		try {
			InputOutput.writeToOutput(compound, output);
		}
		catch (final IOException e) {}
		finally {
			try {
				output.close();
				bytes.close();
			}
			catch (final IOException e) {}
		}
		return bytes.toByteArray();
	}

	public static byte[] getCompressedBytes(final DataCompound compound) throws IOException {
		final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		final DataOutputStream compressed = new DataOutputStream(new GZIPOutputStream(byteArray));
		InputOutput.writeToOutput(compound, compressed);
		compressed.close();
		byteArray.close();
		return byteArray.toByteArray();
	}

	public static void writeToCompressedStream(final DataCompound compound, final OutputStream stream)
			throws IOException {
		final DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(stream)));
		InputOutput.writeToOutput(compound, output);
		output.close();
	}

	private static void writeToOutput(final Data<?> data, final DataOutput output) throws IOException {
		output.writeByte(data.code());
		if (data.code() == 0)
			return;
		output.writeUTF("");
		data.write(output);
	}

	private static final char[] HEX = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(final byte[] bytes) {
		final char[] hexChars = new char[bytes.length * 2];
		for (int i = 0; i < bytes.length; i++) {
			final int v = bytes[i] & 0xFF;
			hexChars[i * 2] = InputOutput.HEX[v >>> 4];
			hexChars[i * 2 + 1] = InputOutput.HEX[v & 0x0F];
		}
		return new String(hexChars);
	}
}
