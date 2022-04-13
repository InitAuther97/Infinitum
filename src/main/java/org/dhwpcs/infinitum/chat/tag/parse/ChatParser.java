package org.dhwpcs.infinitum.chat.tag.parse;

import org.dhwpcs.infinitum.chat.adventure.JoinedTranslatable;
import org.dhwpcs.infinitum.chat.adventure.Translatable;
import org.dhwpcs.infinitum.chat.tag.ChatContext;
import org.dhwpcs.infinitum.chat.tag.ChatTag;
import org.dhwpcs.infinitum.chat.tag.TagInstance;
import org.dhwpcs.infinitum.chat.tag.parse.argument.TagContext;
import org.dhwpcs.infinitum.chat.tag.parse.op.Type;

import java.util.*;

public class ChatParser {
    private Set<ChatTag<?>> tags = new HashSet<>();

    public boolean addTag(ChatTag<?> tag) {
        return tags.add(tag);
    }

    public Translatable parse(ChatContext ctx) throws TagFailedException {
        String raw = ctx.getContent();
        char[] chars = raw.toCharArray();
        char now;
        List<Translatable> memory = new LinkedList<>();
        boolean escape = false;
        boolean string = false;
        boolean inKvNow = false;
        Stack<Boolean> inKv = new Stack<>();
        Stack<TagDataWriter> tagStack = new Stack<>();
        DepthLocal<StringRange> rangeLocal = new DepthLocal<>(StringRange::new);
        StringRange rangeNow = rangeLocal.deeper();
        TagDataWriter tagIn = null;
        for (int i = 0;i < chars.length;i++) {
            now = chars[i];
            if(escape) {
                escape = false;
                continue;
            }
            if (tagIn != null) {
                if (!string && tagIn.getType() != Type.CONTENT) {
                    switch (now) {
                        case '\\' -> {
                            escape = true;
                            continue;
                        }
                        case '"' -> {
                            string = true;
                            continue;
                        }
                        case ' ' -> {
                            rangeNow.setEnd(i);
                            if(!rangeNow.isPoint()) {
                                if(!inKvNow) {
                                    tagIn.write(Type.ATTRIBUTE);
                                }
                                tagIn.write(rangeNow);
                                inKvNow = false;
                            }
                            rangeNow.setBegin(i+1);
                            continue;
                        }
                        case '=' -> {
                            rangeNow.setEnd(i);
                            if(rangeNow.isPoint()) {
                                throw new TagFailedException(TagFailedException.Reason.PARSER_INVALID_EQUAL, "No element before = is found at "+i);
                            } else {
                                tagIn.write(Type.PROPERTY);
                                tagIn.write(rangeNow.<String>transform(raw::substring));
                            }
                            inKvNow = true;
                        }
                        case '/' -> {
                            tagIn.unpaired();
                            continue;
                        }
                    }
                } else {
                    switch (now) {
                        case '"' -> {
                            if(tagIn.getType() != Type.CONTENT) {
                                string = false;
                            }
                            continue;
                        }
                        case '\\' -> {
                            escape = true;
                            continue;
                        }
                    }
                }
            }
            if (now == '{' && !(tagIn != null && chars[i-1] == '\\')) {
                rangeNow.setEnd(now);
                if(!rangeNow.isPoint()) {
                    if (tagIn != null) {
                        tagIn.write(rangeNow.<String>transform(raw::substring));
                        tagStack.push(tagIn);
                    } else {
                        memory.add(Translatable.plain(rangeNow.transform(raw::substring)));
                    }
                    rangeNow.setBegin(now);
                }
                rangeNow = rangeLocal.deeper();
                rangeNow.setBegin(i+1);
                tagIn = new TagDataWriter(i);
                inKv.push(inKvNow);
                inKvNow = false;
            } else if (now == '}') {
                if(tagIn != null) {
                    rangeNow = rangeLocal.shallower();
                    if (tagIn.isTerminated()) {
                        tagIn.setEnd(i);
                        rangeNow.setEnd(i+1);
                        if (tagIn.getId() == null) {
                            if (!tagStack.isEmpty()) {
                                TagDataWriter writer = tagStack.pop();
                                writer.close();
                                tagIn = writer;
                            } else {
                                throw new TagFailedException(TagFailedException.Reason.PARSER_UNDEFINED_TAG, "Found TAG_END before TAG_BEGIN");
                            }
                        }
                        if (!tagStack.isEmpty()) {
                            TagDataWriter writer = tagStack.pop();
                            writer.write(parseTag(tagIn, ctx, rangeNow.transform(raw::substring)));
                            tagIn = writer;
                            inKvNow = inKv.pop();
                        } else {
                            TagInstance<?> inst = parseTag(tagIn, ctx, rangeNow.transform(raw::substring));
                            if (inst == null) {
                                throw new TagFailedException(TagFailedException.Reason.PARSER_UNDEFINED_TAG, "Undefined tag '" + tagIn.getId() + "' from " + tagIn.begin() + " to " + tagIn.end());
                            }
                            memory.add(inst.parse(ctx));
                            tagIn = null;
                        }
                    } else {
                        tagIn.write(Type.CONTENT);
                        rangeNow.setBegin(i+1);
                    }
                }
            }
        }
        if(rangeLocal.hasShallower()) {
            throw new TagFailedException(TagFailedException.Reason.PARSER_UNCLOSED_TAG, "Unclosed tags in message.");
        }
        rangeNow.setEnd(raw.length());
        memory.add(Translatable.plain(rangeNow.transform(raw::substring)));
        return new JoinedTranslatable(memory.toArray(Translatable[]::new));
    }

    public TagInstance<?> parseTag(TagDataWriter writer, ChatContext ctx, String raw) throws TagFailedException {
        if(writer.getId() == null) {
            return TagInstance.END_TAG;
        }
        for(ChatTag<?> tag : tags) {
            if(tag.id().contentEquals(writer.getId()) || tag.aliases().contains(writer.getId())) {
                TagContext context = new TagContext(tag.arguments().parse(writer.getProperties(), ctx), writer.getAttributes(), writer.getContent());
                TagInstance<?> result = new TagInstance<>((ChatTag<Object>) tag, tag.parse(context, ctx), raw);
                return result;
            }
        }
        return null;
    }
}
