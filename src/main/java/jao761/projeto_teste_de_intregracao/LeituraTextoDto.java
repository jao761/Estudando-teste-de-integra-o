package jao761.projeto_teste_de_intregracao;

public record LeituraTextoDto(
        Long id,
        String texto
) {
    public LeituraTextoDto(Texto t) {
        this(t.getId(), t.getTexto());
    }
}
