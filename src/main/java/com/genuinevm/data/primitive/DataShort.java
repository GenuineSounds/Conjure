package com.genuinevm.data.primitive;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;

import com.genuinevm.data.Data;
import com.genuinevm.data.Primitive;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class DataShort implements Data<Short>, Primitive {

	public static final byte CODE = 2;
	private short value;

	public DataShort() {}

	public DataShort(final short value) {
		this.value = value;
	}

	@Override
	public Short value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeShort(value);
	}

	@Override
	public void read(final DataInput in) throws IOException {
		value = in.readShort();
	}

	@Override
	public String toString() {
		return Short.toString(value);
	}

	@Override
	public DataShort copy() {
		return new DataShort(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof Primitive)
			return value().equals(((Primitive) obj).toShort());
		return obj instanceof Number && value().equals(((Number) obj).shortValue());
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public boolean toBoolean() {
		return toByte() != 0;
	}

	@Override
	public long toLong() {
		return value;
	}

	@Override
	public int toInt() {
		return value;
	}

	@Override
	public short toShort() {
		return value;
	}

	@Override
	public byte toByte() {
		return (byte) (value & 255);
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
	public JsonPrimitive serialize(final Data<Short> src, final Type typeOfSrc, final JsonSerializationContext context) {
		return new JsonPrimitive(src.value());
	}

	@Override
	public DataShort deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		try {
			return new DataShort(json.getAsShort());
		}
		catch (final Exception e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public byte code() {
		return DataShort.CODE;
	}
}
