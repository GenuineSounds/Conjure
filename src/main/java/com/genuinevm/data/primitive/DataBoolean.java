package com.genuinevm.data.primitive;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;

import com.genuinevm.data.AbstractData;
import com.genuinevm.data.Primitive;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class DataBoolean extends AbstractData<Boolean> implements Primitive {

	public static final String NAME = "BOOLEAN";
	public static final long SIZE = 1;
	public static final byte TYPE = 12;
	private boolean value;

	public DataBoolean() {}

	public DataBoolean(final boolean value) {
		this.value = value;
	}

	@Override
	public String getTypeName() {
		return NAME;
	}

	@Override
	public byte getTypeByte() {
		return TYPE;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeBoolean(value);
	}

	@Override
	public void read(final DataInput in) throws IOException {
		value = in.readBoolean();
	}

	@Override
	public DataBoolean copy() {
		return new DataBoolean(value);
	}

	@Override
	public Boolean value() {
		return value;
	}

	@Override
	public String toString() {
		return Boolean.toString(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof Primitive)
			return value().equals(((Primitive) obj).toBoolean());
		return obj instanceof Boolean && value().equals(((Boolean) obj).booleanValue());
	}

	@Override
	public int hashCode() {
		return toByte();
	}

	@Override
	public boolean toBoolean() {
		return value;
	}

	@Override
	public long toLong() {
		return value ? 1 : 0;
	}

	@Override
	public int toInt() {
		return value ? 1 : 0;
	}

	@Override
	public short toShort() {
		return (short) (value ? 1 : 0);
	}

	@Override
	public byte toByte() {
		return (byte) (value ? 1 : 0);
	}

	@Override
	public double toDouble() {
		return value ? 1 : 0;
	}

	@Override
	public float toFloat() {
		return value ? 1 : 0;
	}

	@Override
	public JsonPrimitive serialize(final AbstractData<Boolean> src, final Type typeOfSrc, final JsonSerializationContext context) {
		return new JsonPrimitive(src.value());
	}

	@Override
	public DataBoolean deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		return new DataBoolean(json.getAsBoolean());
	}
}
