package com.genuinevm.conjure.data.primitive;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;

import com.genuinevm.conjure.data.Data;
import com.genuinevm.conjure.data.Primitive;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class DataFloat implements Data<Float>, Primitive {

	public static final byte CODE = 5;
	private float value;

	public DataFloat() {}

	public DataFloat(final float value) {
		this.value = value;
	}

	@Override
	public Float value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeFloat(value);
	}

	@Override
	public void read(final DataInput in) throws IOException {
		value = in.readFloat();
	}

	@Override
	public String toString() {
		return Float.toString(value);
	}

	@Override
	public DataFloat copy() {
		return new DataFloat(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof Primitive)
			return value().equals(((Primitive) obj).toFloat());
		return obj instanceof Number && value().equals(((Number) obj).floatValue());
	}

	@Override
	public int hashCode() {
		return Float.floatToIntBits(value);
	}

	@Override
	public boolean toBoolean() {
		return toByte() != 0;
	}

	@Override
	public long toLong() {
		return (long) value;
	}

	@Override
	public int toInt() {
		return (int) Math.floor(value) & 0xFFFFFFFF;
	}

	@Override
	public short toShort() {
		return (short) ((int) Math.floor(value) & 0xFFFF);
	}

	@Override
	public byte toByte() {
		return (byte) ((int) Math.floor(value) & 0xFF);
	}

	@Override
	public double toDouble() {
		return value;
	}

	@Override
	public float toFloat() {
		return value;
	}

	@Override
	public JsonPrimitive serialize(final Data<Float> src, final Type typeOfSrc, final JsonSerializationContext context) {
		return new JsonPrimitive(src.value());
	}

	@Override
	public DataFloat deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		try {
			return new DataFloat(json.getAsFloat());
		} catch (final Exception e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public byte code() {
		return DataFloat.CODE;
	}
}
