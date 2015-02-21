package com.genuinevm.conjure.data.collection;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;

import com.genuinevm.conjure.data.Data;
import com.genuinevm.conjure.data.PrimitiveArray;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class DataByteArray implements Data<byte[]>, PrimitiveArray {

	public static final byte CODE = 7;
	private byte[] value;

	public DataByteArray() {}

	public DataByteArray(final byte[] value) {
		this.value = value;
	}

	@Override
	public byte[] value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeInt(value.length);
		out.write(value);
	}

	@Override
	public void read(final DataInput in) throws IOException {
		final int size = in.readInt();
		value = new byte[size];
		in.readFully(value);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0; i < value.length; i++) {
			sb.append(value[i]);
			sb.append(", ");
		}
		if (sb.indexOf(", ") > 0) {
			sb.deleteCharAt(sb.length() - 1);
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(']');
		return sb.toString();
	}

	@Override
	public DataByteArray copy() {
		final byte[] value = new byte[this.value.length];
		System.arraycopy(this.value, 0, value, 0, this.value.length);
		return new DataByteArray(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof PrimitiveArray)
			return Arrays.equals(value(), ((PrimitiveArray) obj).toByteArray());
		return obj instanceof byte[] && Arrays.equals(value(), (byte[]) obj);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(value);
	}

	@Override
	public JsonArray serialize(final Data<byte[]> src, final Type typeOfSrc, final JsonSerializationContext context) {
		final JsonArray array = new JsonArray();
		for (final byte b : src.value())
			array.add(new JsonPrimitive(b));
		return array;
	}

	@Override
	public DataByteArray deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		try {
			final JsonArray array = json.getAsJsonArray();
			final byte[] bytes = new byte[array.size()];
			for (int i = 0; i < bytes.length; i++)
				bytes[i] = array.get(i).getAsByte();
			return new DataByteArray(bytes);
		}
		catch (final Exception e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public byte[] toByteArray() {
		return value;
	}

	@Override
	public int[] toIntArray() {
		final int[] array = new int[value.length];
		for (int i = 0; i < array.length; i++)
			array[i] = value[i];
		return array;
	}

	@Override
	public byte code() {
		return DataByteArray.CODE;
	}
}