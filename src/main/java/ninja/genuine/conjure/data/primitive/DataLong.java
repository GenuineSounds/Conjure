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

public class DataLong implements Data<Long>, Primitive {

	public static final byte CODE = 4;
	private long value;

	public DataLong() {}

	public DataLong(final long value) {
		this.value = value;
	}

	@Override
	public Long value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeLong(value);
	}

	@Override
	public void read(final DataInput in) throws IOException {
		value = in.readLong();
	}

	@Override
	public String toString() {
		return Long.toString(value);
	}

	@Override
	public DataLong copy() {
		return new DataLong(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof Primitive)
			return value().equals(((Primitive) obj).toLong());
		return obj instanceof Number && value().equals(((Number) obj).longValue());
	}

	@Override
	public int hashCode() {
		return (int) (value ^ value >>> 32);
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
		return (int) (value & 0xFFFFFFFF);
	}

	@Override
	public short toShort() {
		return (short) (value & 0xFFFF);
	}

	@Override
	public byte toByte() {
		return (byte) (value & 0xFF);
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
	public JsonPrimitive serialize(final Data<Long> src, final Type typeOfSrc, final JsonSerializationContext context) {
		return new JsonPrimitive(src.value());
	}

	@Override
	public DataLong deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		try {
			return new DataLong(json.getAsLong());
		} catch (final Exception e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public byte code() {
		return DataLong.CODE;
	}
}
