package com.genuinevm.data.collection;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.genuinevm.data.Data;
import com.genuinevm.data.TypeSystem;
import com.genuinevm.data.util.Serialization;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

@SuppressWarnings("rawtypes")
public class DataList implements Data<List<Data>>, List<Data> {

	public static final byte CODE = 13;
	private List<Data> values = new ArrayList<Data>();

	public DataList() {}

	public DataList(final Collection<? extends Data> col) {
		values = new ArrayList<Data>(col);
	}

	@Override
	public List<Data> value() {
		return values;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeInt(values.size());
		for (int i = 0; i < values.size(); i++) {
			out.writeByte(values.get(i).code());
			values.get(i).write(out);
		}
	}

	@Override
	public void read(final DataInput in) throws IOException {
		final int size = in.readInt();
		values = new ArrayList<Data>();
		for (int i = 0; i < size; i++) {
			Data data = TypeSystem.getTypeSystem().createByCode(in.readByte()).copy();
			data.read(in);
			values.add(data);
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0; i < values.size(); i++) {
			sb.append(values.get(i).value());
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
	public Data<List<Data>> copy() {
		List<Data> newList = new ArrayList<Data>(values.size());
		for (int i = 0; i < newList.size(); i++)
			newList.add(i, values.get(i).copy());
		return new DataList(newList);
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
		return values.hashCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonArray serialize(final Data<List<Data>> src, final Type typeOfSrc, final JsonSerializationContext context) {
		final JsonArray array = new JsonArray();
		for (final Data data : src.value())
			array.add(data.serialize(data, data.getClass(), context));
		return array;
	}

	@Override
	public DataList deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		DataList list = new DataList();
		for (JsonElement jsonElement : json.getAsJsonArray())
			list.add(Serialization.create(jsonElement, jsonElement.getClass(), context));
		return list;
	}

	@Override
	public byte code() {
		return DataArray.CODE;
	}

	public boolean add(final Data data) {
		return values.add(data);
	}

	public boolean addAll(final Collection<? extends Data> col) {
		return values.addAll(col);
	}

	public Data remove(final int index) {
		return values.remove(index);
	}

	public Data get(final int index) {
		return values.get(index);
	}

	public int size() {
		return values.size();
	}

	public Iterator<Data> iterator() {
		return values.iterator();
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return values.contains(o);
	}

	@Override
	public Object[] toArray() {
		return values.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return values.toArray(a);
	}

	@Override
	public boolean remove(Object o) {
		return values.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return values.containsAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Data> c) {
		return values.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return values.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return values.retainAll(c);
	}

	@Override
	public void clear() {
		values.clear();
	}

	@Override
	public Data set(int index, Data data) {
		return values.set(index, data);
	}

	@Override
	public void add(int index, Data data) {
		values.add(index, data);
	}

	@Override
	public int indexOf(Object o) {
		return values.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return values.lastIndexOf(o);
	}

	@Override
	public ListIterator<Data> listIterator() {
		return values.listIterator();
	}

	@Override
	public ListIterator<Data> listIterator(int index) {
		return values.listIterator(index);
	}

	@Override
	public List<Data> subList(int fromIndex, int toIndex) {
		return values.subList(fromIndex, toIndex);
	}
}
