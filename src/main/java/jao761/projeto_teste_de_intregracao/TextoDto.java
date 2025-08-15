package jao761.projeto_teste_de_intregracao;

import org.hibernate.validator.constraints.NotBlank;

public record TextoDto(

        @NotBlank String texto

) {
}
