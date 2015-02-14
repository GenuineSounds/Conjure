package com.genuinevm.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public interface Data<T> extends JsonSerializer<Data<T>>, JsonDeserializer<Data<T>> {

	/**
	 * A unique signature byte code associated with it for serialization.
	 * This will be the single byte that is written to the {@link DataOutput}
	 * and read from the {@link DataInput} as the signature of the data that
	 * follows.
	 *
	 * @return unique byte
	 */
	public byte code();

	/**
	 * Returns the value held by this instance.
	 *
	 * @return T: value
	 */
	public T value();

	/**
	 * Creates and returns a copy of this object.<br>The precise meaning
	 * of "copy" may depend on the class of the object.<br>The general
	 * intent is that, for any {@link Data} {@code x}, the expression:
	 * <pre>
	 * x.copy().equals(x)</pre>
	 * will be true, and the {@link value} will be the same for both.
	 *
	 * @return A copy of this instance.
	 */
	public Data<T> copy();

	/**
	 * Mutate the state of the instance based on the input.<br>Much like
	 * {@link Data.copy} the general intent is that, for any {@link Data}
	 * {@code x} and {@link DataInput} {@code input}, the expression:
	 * <pre>
	 * x.write(input);
	 * x.equals(new Data().read(input));</pre>
	 * will be true, and the {@link value} will be the same for both.
	 *
	 * @param input the {@link DataInput} that is read.
	 * @throws IOException If an I/O error occurs.
	 */
	public void read(DataInput input) throws IOException;

	/**
	 * Write the instance state to a {@link DataOutput}.<br>Much like
	 * {@link Data.copy} the general intent is that, for any {@link Data}
	 * {@code x} and {@link DataInput} {@code input}, the expression:
	 * <pre>
	 * x.write(input);
	 * x.equals(new Data().read(input));</pre>
	 * will be true, and the {@link value} will be the same for both.
	 *
	 * @param output the {@link DataOutput} that is written to.
	 * @throws IOException If an I/O error occurs.
	 */
	public void write(DataOutput output) throws IOException;

	@Override
	public Data<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException;

	@Override
	public JsonElement serialize(Data<T> src, Type typeOfSrc, JsonSerializationContext context);
}
