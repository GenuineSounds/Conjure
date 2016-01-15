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

public class DataIntegerArray implements Data<int[]>, PrimitiveArray {

	public static final byte CODE = 11;
	private int[] value;

	public DataIntegerArray() {}

	public DataIntegerArray(final int[] value) {
		this.value = value;
	}

	@Override
	public int[] value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeInt(value.length);
		for (int i = 0; i < value.length; ++i)
			out.writeInt(value[i]);
	}

	@Override
	public void read(final DataInput in) throws IOException {
		final int size = in.readInt();
		value = new int[size];
		for (int i = 0; i < size; ++i)
			value[i] = in.readInt();
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
	public DataIntegerArray copy() {
		final int[] value = new int[this.value.length];
		System.arraycopy(this.value, 0, value, 0, this.value.length);
		return new DataIntegerArray(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof PrimitiveArray)
			return Arrays.equals(value(), ((PrimitiveArray) obj).toIntArray());
		return obj instanceof int[] && Arrays.equals(value(), (int[]) obj);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(value);
	}

	@Override
	public JsonArray serialize(final Data<int[]> src, final Type typeOfSrc, final JsonSerializationContext context) {
		final JsonArray array = new JsonArray();
		for (final int i : src.value())
			array.add(new JsonPrimitive(i));
		return array;
	}

	@Override
	public DataIntegerArray deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		try {
			final JsonArray array = json.getAsJsonArray();
			final int[] ints = new int[array.size()];
			for (int i = 0; i < ints.length; i++)
				ints[i] = array.get(i).getAsByte();
			return new DataIntegerArray(ints);
		} catch (final Exception e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public byte[] toByteArray() {
		final byte[] array = new byte[value.length];
		for (int i = 0; i < array.length; i++)
			array[i] = (byte) (value[i] & 0xFF);
		return array;
	}

	@Override
	public int[] toIntArray() {
		return value;
	}

	@Override
	public byte code() {
		return DataIntegerArray.CODE;
	}
}
