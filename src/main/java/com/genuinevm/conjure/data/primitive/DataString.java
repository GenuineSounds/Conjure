package com.genuinevm.conjure.data.primitive;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.commons.lang3.StringEscapeUtils;

import com.genuinevm.conjure.data.Data;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class DataString implements Data<String> {

	public static final byte CODE = 8;
	private String value = "";

	public DataString() {}

	public DataString(final String value) {
		this.value = value;
		if (value == null)
			throw new IllegalArgumentException("Null value not allowed");
	}

	@Override
	public String value() {
		return value;
	}

	@Override
	public void read(final DataInput in) throws IOException {
		value = in.readUTF();
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeUTF(value);
	}

	@Override
	public String toString() {
		return "\"" + StringEscapeUtils.escapeJson(value) + "\"";
	}

	@Override
	public DataString copy() {
		return new DataString(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof Data)
			return value().equals(((Data<?>) obj).value());
		return value().equals(obj);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public JsonPrimitive serialize(final Data<String> src, final Type typeOfSrc, final JsonSerializationContext context) {
		return new JsonPrimitive(src.value());
	}

	@Override
	public DataString deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		try {
			return new DataString(json.getAsString());
		} catch (final Exception e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public byte code() {
		return DataString.CODE;
	}
}
