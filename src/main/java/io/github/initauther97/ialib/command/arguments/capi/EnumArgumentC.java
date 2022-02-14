package io.github.initauther97.ialib.command.arguments.capi;

import dev.jorel.commandapi.arguments.CustomArgument;
import io.github.initauther97.ialib.command.arguments.EnumFindResult;

public class EnumArgumentC<E extends Enum<E>> implements CustomArgument.CustomArgumentInfoParser<EnumFindResult<E>> {

    private final Class<E> eclz;
    public EnumArgumentC(Class<E> eclz) {
        this.eclz = eclz;
    }

    @Override
    public EnumFindResult<E> apply(CustomArgument.CustomArgumentInfo customArgumentInfo) {
        return EnumFindResult.findEnum(eclz, customArgumentInfo.input());
    }
}
