package ninja.genuine.conjure.data.primitive;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import ninja.genuine.conjure.data.Data;
import ninja.genuine.conjure.data.Primitive;

public class DataInteger implements Data<Integer>, Primitive {

	public static final byte CODE = 3;
	private int value;

	public DataInteger() {}

	public DataInteger(final int value) {
		this.value = value;
	}

	@Override
	public Integer value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeInt(value);
	}

	@Override
	public void read(final DataInput in) throws IOException {
		value = in.readInt();
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

	@Override
	public DataInteger copy() {
		return new DataInteger(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof Primitive)
			return value().equals(((Primitive) obj).toInt());
		return obj instanceof Number && value().equals(((Number) obj).intValue());
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
		return (short) (value & 65535);
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
	public JsonPrimitive serialize(final Data<Integer> src, final Type typeOfSrc, final JsonSerializationContext context) {
		return new JsonPrimitive(src.value());
	}

	@Override
	public DataInteger deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		try {
			return new DataInteger(json.getAsInt());
		} catch (final Exception e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public byte code() {
		return DataInteger.CODE;
	}
}
