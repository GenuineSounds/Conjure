package ninja.genuine.conjure.data.collection;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

import ninja.genuine.conjure.TypeSystem;
import ninja.genuine.conjure.data.Data;
import ninja.genuine.conjure.data.Deserializer;
import ninja.genuine.conjure.data.Primitive;
import ninja.genuine.conjure.data.PrimitiveArray;
import ninja.genuine.conjure.data.primitive.DataBoolean;
import ninja.genuine.conjure.data.primitive.DataByte;
import ninja.genuine.conjure.data.primitive.DataDouble;
import ninja.genuine.conjure.data.primitive.DataFloat;
import ninja.genuine.conjure.data.primitive.DataInteger;
import ninja.genuine.conjure.data.primitive.DataLong;
import ninja.genuine.conjure.data.primitive.DataShort;
import ninja.genuine.conjure.data.primitive.DataString;

@SuppressWarnings("rawtypes")
public class DataCompound implements Data<Map<String, Data>> {

	public static final byte CODE = 10;
	private Map<String, Data> values = new HashMap<String, Data>();

	public DataCompound() {}

	public DataCompound(final Map<String, Data> values) {
		this.values = values;
	}

	@Override
	public Map<String, Data> value() {
		return values;
	}

	@Override
	public void write(final DataOutput output) throws IOException {
		for (final String name : values.keySet()) {
			final Data data = values.get(name);
			output.writeByte(data.code());
			if (data.code() != 0) {
				output.writeUTF(name);
				data.write(output);
			}
		}
		output.writeByte(0);
	}

	@Override
	public void read(final DataInput input) throws IOException {
		values.clear();
		byte type;
		while ((type = input.readByte()) != 0) {
			final String name = input.readUTF();
			final Data data = TypeSystem.getTypeSystem().createByCode(type);
			data.read(input);
			values.put(name, data);
		}
	}

	public Set<String> getEntryNames() {
		return values.keySet();
	}

	public void set(final String name, final Data value) {
		values.put(name, value);
	}

	public void set(final String name, final boolean value) {
		values.put(name, value ? DataBoolean.TRUE : DataBoolean.FALSE);
	}

	public void set(final String name, final byte value) {
		values.put(name, new DataByte(value));
	}

	public void set(final String name, final short value) {
		values.put(name, new DataShort(value));
	}

	public void set(final String name, final int value) {
		values.put(name, new DataInteger(value));
	}

	public void set(final String name, final long value) {
		values.put(name, new DataLong(value));
	}

	public void set(final String name, final float value) {
		values.put(name, new DataFloat(value));
	}

	public void set(final String name, final double value) {
		values.put(name, new DataDouble(value));
	}

	public void set(final String name, final String value) {
		values.put(name, new DataString(value));
	}

	public void set(final String name, final byte[] value) {
		values.put(name, new DataByteArray(value));
	}

	public void set(final String name, final int[] value) {
		values.put(name, new DataIntegerArray(value));
	}

	public void set(final String name, final BigInteger value) {
		values.put(name, new DataByteArray(value.toByteArray()));
	}

	public void set(final String name, final BigDecimal value) {
		values.put(name, new DataString(value.toString()));
	}

	public byte getDataCode(final String name) {
		final Data nbtbase = values.get(name);
		return nbtbase != null ? nbtbase.code() : 0;
	}

	public boolean hasEntry(final String name) {
		return values.containsKey(name);
	}

	public boolean entryIsDataCode(final String name, final int withType) {
		final byte type = getDataCode(name);
		return type == withType;
	}

	public Data getData(final String name) {
		return values.get(name);
	}

	public boolean getBoolean(final String name) {
		try {
			return values.containsKey(name) ? ((Primitive) values.get(name)).toBoolean() : false;
		} catch (final ClassCastException e) {
			return false;
		}
	}

	public byte getByte(final String name) {
		try {
			return values.containsKey(name) ? ((Primitive) values.get(name)).toByte() : 0;
		} catch (final ClassCastException e) {
			return (byte) 0;
		}
	}

	public short getShort(final String name) {
		try {
			return values.containsKey(name) ? ((Primitive) values.get(name)).toShort() : 0;
		} catch (final ClassCastException e) {
			return (short) 0;
		}
	}

	public int getInteger(final String name) {
		try {
			return values.containsKey(name) ? ((Primitive) values.get(name)).toInt() : 0;
		} catch (final ClassCastException classcastexception) {
			return 0;
		}
	}

	public long getLong(final String name) {
		try {
			return values.containsKey(name) ? ((Primitive) values.get(name)).toLong() : 0;
		} catch (final ClassCastException e) {
			return 0;
		}
	}

	public float getFloat(final String name) {
		try {
			return values.containsKey(name) ? ((Primitive) values.get(name)).toFloat() : 0;
		} catch (final ClassCastException classcastexception) {
			return 0;
		}
	}

	public double getDouble(final String name) {
		try {
			return values.containsKey(name) ? ((Primitive) values.get(name)).toDouble() : 0;
		} catch (final ClassCastException classcastexception) {
			return 0;
		}
	}

	public String getString(final String name) {
		try {
			return values.containsKey(name) ? (values.get(name) instanceof DataString ? ((DataString) values.get(name)).value() : values.get(name).toString()) : "";
		} catch (final ClassCastException classcastexception) {
			return "";
		}
	}

	public byte[] getByteArray(final String name) {
		try {
			return values.containsKey(name) ? ((PrimitiveArray) values.get(name)).toByteArray() : new byte[0];
		} catch (final ClassCastException e) {
			return new byte[0];
		}
	}

	public int[] getIntArray(final String name) {
		try {
			return values.containsKey(name) ? ((PrimitiveArray) values.get(name)).toIntArray() : new int[0];
		} catch (final ClassCastException e) {
			return new int[0];
		}
	}

	public BigInteger getBigInteger(final String name) {
		try {
			return values.containsKey(name) ? new BigInteger(((DataByteArray) values.get(name)).value()) : new BigInteger("0");
		} catch (final ClassCastException e) {
			return new BigInteger("0");
		}
	}

	public BigDecimal getBigDecimal(final String name) {
		try {
			return values.containsKey(name) ? new BigDecimal(((DataString) values.get(name)).value()) : new BigDecimal("0");
		} catch (final ClassCastException e) {
			return new BigDecimal("0");
		}
	}

	public DataArray getArray(final String name) {
		try {
			return values.containsKey(name) ? (DataArray) values.get(name) : new DataArray();
		} catch (final ClassCastException e) {
			return new DataArray();
		}
	}

	public DataCompound getCompound(final String name) {
		try {
			return values.containsKey(name) ? (DataCompound) values.get(name) : new DataCompound();
		} catch (final ClassCastException e) {
			return new DataCompound();
		}
	}

	public void remove(final String name) {
		values.remove(name);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		for (final Entry<String, Data> entry : values.entrySet()) {
			sb.append('"');
			sb.append(StringEscapeUtils.escapeJson(entry.getKey()));
			sb.append("\": ");
			sb.append(entry.getValue());
			sb.append(", ");
		}
		if (sb.indexOf(", ") > 0) {
			sb.deleteCharAt(sb.length() - 1);
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(" }");
		if (sb.indexOf("  ") > 0) {
			sb.deleteCharAt(sb.length() - 2);
			sb.deleteCharAt(sb.length() - 2);
		}
		return sb.toString();
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public DataCompound copy() {
		final DataCompound compound = new DataCompound();
		for (final String key : values.keySet())
			compound.set(key, values.get(key).copy());
		return compound;
	}

	@Override
	public boolean equals(final Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof Data)
			return value().equals(((Data) obj).value());
		return obj instanceof Map && value().entrySet().equals(((Map) obj).entrySet());
	}

	@Override
	public int hashCode() {
		return values.hashCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonObject serialize(final Data<Map<String, Data>> src, final Type typeOfSrc, final JsonSerializationContext context) {
		final JsonObject object = new JsonObject();
		for (final Entry<String, Data> entry : src.value().entrySet())
			object.add(entry.getKey(), entry.getValue().serialize(entry.getValue(), entry.getValue().getClass(), context));
		return object;
	}

	@Override
	public DataCompound deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		try {
			final Map<String, Data> map = new HashMap<String, Data>();
			for (final Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet())
				map.put(entry.getKey(), Deserializer.parse(entry.getValue(), entry.getValue().getClass(), context));
			return new DataCompound(map);
		} catch (final Exception e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public byte code() {
		return DataCompound.CODE;
	}
}
