package com.genuinevm.conjure.data;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.genuinevm.conjure.data.collection.DataArray;
import com.genuinevm.conjure.data.collection.DataByteArray;
import com.genuinevm.conjure.data.collection.DataCompound;
import com.genuinevm.conjure.data.collection.DataIntegerArray;
import com.genuinevm.conjure.data.collection.DataList;
import com.genuinevm.conjure.data.primitive.DataBoolean;
import com.genuinevm.conjure.data.primitive.DataByte;
import com.genuinevm.conjure.data.primitive.DataDouble;
import com.genuinevm.conjure.data.primitive.DataFloat;
import com.genuinevm.conjure.data.primitive.DataInteger;
import com.genuinevm.conjure.data.primitive.DataLong;
import com.genuinevm.conjure.data.primitive.DataShort;
import com.genuinevm.conjure.data.primitive.DataString;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class Deserializer {

	public static Data<?> parse(final JsonElement element, final Type typeOfT, final JsonDeserializationContext context)
			throws JsonParseException {
		if (element.isJsonObject())
			return new DataCompound().deserialize(element, DataCompound.class, context);
		if (element.isJsonArray())
			return Deserializer.determineCollection(element.getAsJsonArray(), context);
		final JsonPrimitive primitive = element.getAsJsonPrimitive();
		if (primitive.isBoolean())
			return primitive.getAsBoolean() ? DataBoolean.TRUE : DataBoolean.FALSE;
		if (primitive.isNumber())
			return Deserializer.determineNumber(primitive, context);
		if (primitive.isString())
			return new DataString().deserialize(element, DataString.class, context);
		throw new JsonParseException("Enable to deserialize element " + element);
	}

	private static Data<?> determineNumber(final JsonPrimitive primitive, final JsonDeserializationContext context) {
		final String strNumber = primitive.getAsNumber().toString();
		final Class<? extends Number> number = strNumber.contains(".") ? Deserializer.handleFloatingPoint(strNumber)
				: Deserializer.handleInteger(strNumber);
		if (number == Byte.class)
			return new DataByte().deserialize(primitive, DataByte.class, context);
		if (number == Short.class)
			return new DataShort().deserialize(primitive, DataShort.class, context);
		if (number == Integer.class)
			return new DataInteger().deserialize(primitive, DataInteger.class, context);
		if (number == Float.class)
			return new DataFloat().deserialize(primitive, DataFloat.class, context);
		if (number == Double.class)
			return new DataDouble().deserialize(primitive, DataDouble.class, context);
		if (number == Long.class)
			return new DataLong().deserialize(primitive, DataLong.class, context);
		if (number == BigInteger.class)
			return new DataByteArray(primitive.getAsBigInteger().toByteArray());
		if (number == BigDecimal.class)
			return new DataString(primitive.getAsBigDecimal().toPlainString());
		throw new JsonParseException("Enable to determine Number value. " + number.getClass());
	}

	private static Data<?> determineCollection(final JsonArray jsonArray, final JsonDeserializationContext context) {
		if (jsonArray.size() < 2 || Deserializer.isBooleanArray(jsonArray, context))
			return new DataArray().deserialize(jsonArray, DataArray.class, context);
		if (Deserializer.isByteArray(jsonArray, context))
			return new DataByteArray().deserialize(jsonArray, DataByteArray.class, context);
		if (Deserializer.isIntegerArray(jsonArray, context))
			return new DataIntegerArray().deserialize(jsonArray, DataIntegerArray.class, context);
		if (Deserializer.isArray(jsonArray, context))
			return new DataArray().deserialize(jsonArray, DataArray.class, context);
		return new DataList().deserialize(jsonArray, DataList.class, context);
	}

	private static boolean isBooleanArray(final JsonArray jsonArray, final JsonDeserializationContext context) {
		for (int i = 0; i < jsonArray.size(); i++) {
			final Data<?> data = Deserializer.parse(jsonArray.get(i), jsonArray.get(i).getClass(), context);
			if (!(data instanceof DataBoolean))
				return false;
		}
		return true;
	}

	private static boolean isByteArray(final JsonArray jsonArray, final JsonDeserializationContext context) {
		for (int i = 0; i < jsonArray.size(); i++) {
			final Data<?> data = Deserializer.parse(jsonArray.get(i), jsonArray.get(i).getClass(), context);
			if (!(data instanceof Primitive))
				return false;
			final Primitive prim = (Primitive) data;
			if (!data.value().equals(prim.toByte()))
				return false;
		}
		return true;
	}

	private static boolean isIntegerArray(final JsonArray jsonArray, final JsonDeserializationContext context) {
		for (int i = 0; i < jsonArray.size(); i++) {
			final Data<?> data = Deserializer.parse(jsonArray.get(i), jsonArray.get(i).getClass(), context);
			if (!(data instanceof Primitive))
				return false;
			final Primitive prim = (Primitive) data;
			if (!data.value().equals(prim.toInt()))
				return false;
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	private static boolean isArray(final JsonArray jsonArray, final JsonDeserializationContext context) {
		final Class<? extends Data> clazz = Deserializer.parse(jsonArray.get(0), jsonArray.get(0).getClass(), context)
				.getClass();
		for (int i = 1; i < jsonArray.size(); i++)
			if (clazz != Deserializer.parse(jsonArray.get(i), jsonArray.get(i).getClass(), context).getClass())
				return false;
		return true;
	}

	private static Class<? extends Number> handleFloatingPoint(String input) {
		if (input.startsWith("."))
			input = "0" + input;
		if (input.endsWith("."))
			input = input + "0";
		try {
			if (Float.toString(Float.parseFloat(input)).equals(input))
				return Float.class;
			if (Double.toString(Double.parseDouble(input)).equals(input))
				return Double.class;
		}
		catch (final NumberFormatException e) {}
		try {
			return BigDecimal.class;
		}
		catch (final NumberFormatException e) {}
		return null;
	}

	private static Class<? extends Number> handleInteger(final String input) {
		try {
			final Long value = Long.decode(input);
			if (value.longValue() >= Byte.MIN_VALUE && value.longValue() <= Byte.MAX_VALUE)
				return Byte.class;
			else if (value.longValue() >= Short.MIN_VALUE && value.longValue() <= Short.MAX_VALUE)
				return Short.class;
			else if (value.longValue() >= Integer.MIN_VALUE && value.longValue() <= Integer.MAX_VALUE)
				return Integer.class;
			return Long.class;
		}
		catch (final NumberFormatException e) {}
		try {
			return BigInteger.class;
		}
		catch (final NumberFormatException e) {}
		return null;
	}
}
