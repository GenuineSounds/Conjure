package com.genuinevm.data.util;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.genuinevm.data.Data;
import com.genuinevm.data.Primitive;
import com.genuinevm.data.collection.DataArray;
import com.genuinevm.data.collection.DataByteArray;
import com.genuinevm.data.collection.DataCompound;
import com.genuinevm.data.collection.DataIntegerArray;
import com.genuinevm.data.collection.DataList;
import com.genuinevm.data.primitive.DataBoolean;
import com.genuinevm.data.primitive.DataByte;
import com.genuinevm.data.primitive.DataDouble;
import com.genuinevm.data.primitive.DataFloat;
import com.genuinevm.data.primitive.DataInteger;
import com.genuinevm.data.primitive.DataLong;
import com.genuinevm.data.primitive.DataNull;
import com.genuinevm.data.primitive.DataShort;
import com.genuinevm.data.primitive.DataString;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class Serialization {

	public static Data<?> create(final JsonElement element, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		Data<?> out = DataNull.INSTANCE;
		if (element.isJsonObject())
			// Push down object as DataCompound for deserialization
			out = new DataCompound();
		else if (element.isJsonArray())
			// Determine collection to be used. Content aware.
			out = Serialization.determineCollection(element.getAsJsonArray(), context);
		// Must be a primitive.
		else {
			final JsonPrimitive primitive = element.getAsJsonPrimitive();
			if (primitive.isBoolean())
				// No need to push a boolean.
				return primitive.getAsBoolean() ? DataBoolean.TRUE : DataBoolean.FALSE;
			else
				try {
					final Number number = Serialization.convertToSmallestNumber(primitive.getAsNumber().toString());
					// Push down primitives for deserialization
					if (number instanceof Byte)
						out = new DataByte();
					else if (number instanceof Short)
						out = new DataShort();
					else if (number instanceof Integer)
						out = new DataInteger();
					else if (number instanceof Float)
						out = new DataFloat();
					else if (number instanceof Double)
						out = new DataDouble();
					else if (number instanceof Long)
						out = new DataLong();
					// If no valid number was found and pushed then check for special case.
					// These return since the deserialization strategy is not in that Data class.
					if (out.equals(DataNull.INSTANCE)) {
						if (number instanceof BigInteger)
							return new DataByteArray(primitive.getAsBigInteger().toByteArray());
						else if (number instanceof BigDecimal)
							return new DataString(primitive.getAsBigDecimal().toPlainString());
						throw new JsonParseException("Enable to determine Number value." + number.getClass());
					}
				}
				catch (final NumberFormatException e) {}
			if (out.equals(DataNull.INSTANCE) && primitive.isString())
				// Push Strings for deserialization
				out = new DataString();
		}
		return out.deserialize(element, out.getClass(), context);
	}

	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	public static Data<?> determineCollection(final JsonArray jsonArray, final JsonDeserializationContext context) {
		// It doesn't matter what the container is if there is zero or one objects in it.
		if (jsonArray.size() < 2)
			return new DataArray();
		// Check for boolean array first. This would otherwise mess up if there was one boolean in an array of other primitives.
		try {
			for (int i = 0; i < jsonArray.size(); i++)
				if (!(Serialization.create(jsonArray.get(i), jsonArray.get(i).getClass(), context) instanceof DataBoolean))
					throw new Exception();
			return new DataArray();
		}
		catch (final Exception e) {}
		// Check for byte array.
		try {
			for (int i = 0; i < jsonArray.size(); i++) {
				final Data data = Serialization.create(jsonArray.get(i), jsonArray.get(i).getClass(), context);
				if (!(data instanceof Primitive))
					throw new Exception();
				final Primitive prim = (Primitive) data;
				if (!data.value().equals(prim.toByte()))
					throw new Exception();
			}
			return new DataByteArray();
		}
		catch (final Exception e) {}
		// check for int array.
		try {
			for (int i = 0; i < jsonArray.size(); i++) {
				final Data data = Serialization.create(jsonArray.get(i), jsonArray.get(i).getClass(), context);
				if (!(data instanceof Primitive))
					throw new Exception();
				final Primitive prim = (Primitive) data;
				if (!data.value().equals(prim.toInt()))
					throw new Exception();
			}
			return new DataIntegerArray();
		}
		catch (final Exception e) {}
		// Any other types to see if all are the same class.
		try {
			final Class<? extends Data<?>> clazz = (Class<? extends Data<?>>) Serialization.create(jsonArray.get(0), jsonArray.get(0).getClass(), context).getClass();
			for (int i = 1; i < jsonArray.size(); i++)
				if (clazz != Serialization.create(jsonArray.get(i), jsonArray.get(i).getClass(), context).getClass())
					throw new Exception();
			return new DataArray();
		}
		catch (final Exception e) {}
		// Default container.
		return new DataList();
	}

	public static Number convertToSmallestNumber(String input) {
		if (input.contains(".")) {
			if (input.startsWith("."))
				input = "0" + input;
			if (input.endsWith("."))
				input = input + "0";
			try {
				final Float fValue = Float.parseFloat(input);
				if (fValue.toString().equals(input))
					return fValue;
				final Double dValue = Double.parseDouble(input);
				if (dValue.toString().equals(input))
					return dValue;
			}
			catch (final NumberFormatException e) {}
			return new BigDecimal(input);
		}
		try {
			final Long value = Long.decode(input);
			if (value.longValue() >= Byte.MIN_VALUE && value.longValue() <= Byte.MAX_VALUE)
				return Byte.decode(input);
			else if (value.longValue() >= Short.MIN_VALUE && value.longValue() <= Short.MAX_VALUE)
				return Short.decode(input);
			else if (value.longValue() >= Integer.MIN_VALUE && value.longValue() <= Integer.MAX_VALUE)
				return Integer.decode(input);
			return value;
		}
		catch (final NumberFormatException e) {
			return new BigInteger(input);
		}
	}
}
