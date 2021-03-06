package ninja.genuine.conjure.data.collection;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

import ninja.genuine.conjure.TypeSystem;
import ninja.genuine.conjure.data.Data;
import ninja.genuine.conjure.data.Deserializer;
import ninja.genuine.conjure.data.primitive.DataNull;

@SuppressWarnings("rawtypes")
public class DataArray implements Data<Data[]> {

	public static final byte CODE = 9;
	private Data[] values = new Data[0];
	private byte arrayType = DataNull.CODE;

	public DataArray() {}

	public DataArray(final int ofSize) {
		values = new Data[ofSize];
	}

	public DataArray(final Data[] array) {
		values = array;
		setType();
	}

	public DataArray(final Collection<? extends Data> col) {
		this(col.size());
		col.toArray(values);
		setType();
	}

	private void setType() {
		if (values.length > 0)
			arrayType = values[0].code();
	}

	@Override
	public Data[] value() {
		return values;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeByte(arrayType);
		out.writeInt(values.length);
		for (int i = 0; i < values.length; i++)
			values[i].write(out);
	}

	@Override
	public void read(final DataInput in) throws IOException {
		arrayType = in.readByte();
		final Data template = TypeSystem.getTypeSystem().createByCode(arrayType);
		final int size = in.readInt();
		values = new Data[size];
		for (int i = 0; i < size; i++) {
			values[i] = template.copy();
			values[i].read(in);
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0; i < values.length; i++) {
			sb.append(values[i].value());
			sb.append(", ");
		}
		if (sb.indexOf(", ") > 0) {
			sb.deleteCharAt(sb.length() - 1);
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(']');
		return sb.toString();
	}

	public void add(final Data data) {
		if (arrayType == 0)
			arrayType = data.code();
		if (arrayType != data.code())
			return;
		values = Arrays.copyOf(values, values.length + 1);
		values[values.length - 1] = data;
	}

	public Data replace(final int index, final Data data) {
		if (arrayType == data.code() && index >= 0 && index < values.length)
			return values[index] = data;
		return null;
	}

	public Data get(final int index) {
		if (index >= 0 && index < values.length)
			return values[index];
		return null;
	}

	public int length() {
		return values.length;
	}

	@Override
	public Data<Data[]> copy() {
		return new DataArray(Arrays.copyOf(values, values.length));
	}

	@Override
	public boolean equals(final Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof Data)
			return value().equals(((Data) obj).value());
		return obj instanceof List && value().equals(obj);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(values);
	}

	public byte getArrayType() {
		return arrayType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonArray serialize(final Data<Data[]> src, final Type typeOfSrc, final JsonSerializationContext context) {
		final JsonArray array = new JsonArray();
		for (final Data data : src.value())
			array.add(data.serialize(data, data.getClass(), context));
		return array;
	}

	@Override
	public DataArray deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		try {
			final JsonArray jsonArray = json.getAsJsonArray();
			final Data[] array = new Data[jsonArray.size()];
			for (int i = 0; i < array.length; i++) {
				final JsonElement element = jsonArray.get(i);
				array[i] = Deserializer.parse(element, element.getClass(), context);
			}
			return new DataArray(array);
		} catch (final Exception e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public byte code() {
		return DataArray.CODE;
	}
}
