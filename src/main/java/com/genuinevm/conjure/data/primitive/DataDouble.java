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

public class DataDouble implements Data<Double>, Primitive {

	public static final byte CODE = 6;
	private double value;

	public DataDouble() {}

	public DataDouble(final double value) {
		this.value = value;
	}

	@Override
	public Double value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeDouble(value);
	}

	@Override
	public void read(final DataInput in) throws IOException {
		value = in.readDouble();
	}

	@Override
	public String toString() {
		return Double.toString(value);
	}

	@Override
	public DataDouble copy() {
		return new DataDouble(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof Primitive)
			return value().equals(((Primitive) obj).toDouble());
		return obj instanceof Number && value().equals(((Number) obj).doubleValue());
	}

	@Override
	public int hashCode() {
		final long i = Double.doubleToLongBits(value);
		return super.hashCode() ^ (int) (i ^ i >>> 32);
	}

	@Override
	public boolean toBoolean() {
		return toByte() != 0;
	}

	@Override
	public long toLong() {
		return (long) Math.floor(value);
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
		return (float) value;
	}

	@Override
	public JsonPrimitive serialize(final Data<Double> src, final Type typeOfSrc,
			final JsonSerializationContext context) {
		return new JsonPrimitive(src.value());
	}

	@Override
	public DataDouble deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
			throws JsonParseException {
		try {
			return new DataDouble(json.getAsDouble());
		}
		catch (final Exception e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public byte code() {
		return DataDouble.CODE;
	}
}
