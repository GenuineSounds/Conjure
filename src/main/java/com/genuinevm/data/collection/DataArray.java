package com.genuinevm.data.collection;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.genuinevm.data.Data;
import com.genuinevm.data.Primitive;
import com.genuinevm.data.TypeSystem;
import com.genuinevm.data.primitive.DataByte;
import com.genuinevm.data.primitive.DataInteger;
import com.genuinevm.data.primitive.DataNull;
import com.genuinevm.data.util.Serialization;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

public class DataArray implements Data<Data[]> {

	public static final byte TYPE = 9;
	private Data[] values = new Data[0];
	private byte elementType = DataNull.TYPE;

	public DataArray() {}

	public DataArray(final int ofSize) {
		values = new Data[ofSize];
	}

	public DataArray(final Data[] array) {
		values = array;
	}

	public DataArray(final Collection<? extends Data> col) {
		this(col.size());
		col.toArray(values);
	}

	private void setType() {
		if (values.length > 0)
			elementType = values[0].code();
	}

	@Override
	public Data[] value() {
		return values;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeByte(elementType);
		out.writeInt(values.length);
		for (int i = 0; i < values.length; i++)
			values[i].write(out);
	}

	@Override
	public void read(final DataInput in) throws IOException {
		elementType = in.readByte();
		final int size = in.readInt();
		values = new Data[size];
		for (int i = 0; i < size; i++) {
			final Data data = TypeSystem.getTypeSystem().createByCode(elementType);
			data.read(in);
			values[i] = data;
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
		if (data == DataNull.INSTANCE)
			return;
		if (elementType == DataNull.TYPE)
			elementType = data.code();
		if (elementType != data.code())
			return;
		values = Arrays.copyOf(values, values.length + 1);
		values[values.length - 1] = data;
	}

	private Data replace(final int index, final Data data) {
		if (elementType == data.code() && index >= 0 && index < values.length)
			return values[index] = data;
		return DataNull.INSTANCE;
	}

	public Data get(final int index) {
		if (index >= 0 && index < values.length)
			return values[index];
		return DataNull.INSTANCE;
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
		return elementType;
	}

	@Override
	public JsonArray serialize(final Data<Data[]> src, final Type typeOfSrc, final JsonSerializationContext context) {
		final JsonArray array = new JsonArray();
		for (final Data data : src.value())
			array.add(data.serialize(data, data.getClass(), context));
		return array;
	}

	@Override
	public Data deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		final JsonArray jsonArray = json.getAsJsonArray();
		final Data[] array = new Data[jsonArray.size()];
		boolean allDataByte = true;
		boolean allDataInteger = true;
		for (int i = 0; i < array.length; i++) {
			final JsonElement element = jsonArray.get(i);
			final Data data = Serialization.create(element, element.getClass(), context);
			if (data instanceof DataInteger || data instanceof DataByte)
				allDataByte &= data instanceof DataByte;
			else
				allDataByte = allDataInteger = false;
			array[i] = data;
		}
		if (allDataByte) {
			final byte[] bytesOut = new byte[array.length];
			for (int i = 0; i < array.length; i++)
				bytesOut[i] = ((Primitive) array[i]).toByte();
			return new DataByteArray(bytesOut);
		} else if (allDataInteger) {
			final int[] intsOut = new int[array.length];
			for (int i = 0; i < array.length; i++)
				intsOut[i] = ((Primitive) array[i]).toInt();
			return new DataIntegerArray(intsOut);
		}
		return new DataArray(array);
	}

	@Override
	public byte code() {
		return TYPE;
	}
}
