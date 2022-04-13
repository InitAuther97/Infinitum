package org.dhwpcs.infinitum.chat.compile;

import io.github.initauther97.nugget.util.RangeTree;

public record CompilableSection<T> (Compilable<T> compilable,
          int begin, int end) implements RangeTree.Range {}
