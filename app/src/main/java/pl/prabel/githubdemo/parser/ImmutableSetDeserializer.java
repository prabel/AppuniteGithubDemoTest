package pl.prabel.githubdemo.parser;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Set;

public class ImmutableSetDeserializer implements JsonDeserializer<ImmutableSet<?>> {

    @Override
    public ImmutableSet<?> deserialize(JsonElement json,
                                        Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {
        @SuppressWarnings("unchecked")
        final TypeToken<ImmutableSet<?>> immutableListToken = (TypeToken<ImmutableSet<?>>) TypeToken.of(typeOfT);
        final TypeToken<? super ImmutableSet<?>> setToken = immutableListToken.getSupertype(Set.class);
        final Set<?> set = context.deserialize(json, setToken.getType());
        return ImmutableSet.copyOf(set);
    }
}
