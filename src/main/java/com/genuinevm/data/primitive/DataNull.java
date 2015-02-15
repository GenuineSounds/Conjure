package com.genuinevm.data.primitive;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;

import com.genuinevm.data.Data;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

public class DataNull implements Data<Void> {

	public static final DataNull INSTANCE = new DataNull();
	public static final byte CODE = 0;

	private DataNull() {}

	@Override
	public void read(final DataInput in) throws IOException {}

	@Override
	public void write(final DataOutput out) throws IOException {}

	@Override
	public String toString() {
		return "null";
	}

	@Override
	public Data<Void> copy() {
		return DataNull.INSTANCE;
	}

	@Override
	public boolean equals(final Object obj) {
		return super.equals(DataNull.INSTANCE);
	}

	@Override
	public Void value() {
		return null;
	}

	@Override
	public JsonNull serialize(final Data<Void> src, final Type typeOfSrc, final JsonSerializationContext context) {
		return JsonNull.INSTANCE;
	}

	@Override
	public DataNull deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		return DataNull.INSTANCE;
	}

	@Override
	public byte code() {
		return DataNull.CODE;
	}
}