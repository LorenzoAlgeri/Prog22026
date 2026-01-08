package macchinette;

import java.util.Objects;
import java.util.Optional;

/**
 * Strategia che alterna tra monete di valore alto e basso.
 *
 * <p>
 * L'idea qui è alternare tra monete grandi e piccole perché mi sembrava
 * una via di mezzo tra le altre due strategie, comincio con una moneta grande
 * per "buttare giù" il resto,
 * poi ne prendo una piccola per aggiustare meglio, e così via alternando
 */
public class StrategiaRestoAlternata implements StrategiaResto {

  public static final StrategiaRestoAlternata INSTANCE = new StrategiaRestoAlternata();

  private StrategiaRestoAlternata() {
  }

  @Override
  public Optional<Aggregato> calcola(Importo resto, Aggregato disponibile) {
    Objects.requireNonNull(resto);
    Objects.requireNonNull(disponibile);

    if (resto.equals(Importo.ZERO))
      return Optional.of(new Aggregato());
    if (disponibile.valoreTotale().minoreDi(resto))
      return Optional.empty();

    Aggregato risultato = new Aggregato();
    int rimanente = resto.inCentesimi();

    Moneta[] monete = Moneta.values();
    int basso = 0;
    int alto = monete.length - 1;
    boolean usaAlto = true; // Inizia dalle monete grandi

    while (rimanente > 0 && basso <= alto) {
      int indice;
      if (usaAlto) {
        indice = alto;
      } else {
        indice = basso;
      }
      Moneta moneta = monete[indice];
      int valore = moneta.valore().inCentesimi();
      int disponibili = disponibile.quantita(moneta);

      if (valore <= rimanente && disponibili > 0) {
        risultato.aggiungi(moneta, 1);
        rimanente -= valore;
      } else {
        if (usaAlto) {
          alto--;
        } else {
          basso++;
        }
      }

      // Alterna tra alto e basso
      usaAlto = !usaAlto;
    }

    if (rimanente > 0)
      return Optional.empty();

    return Optional.of(risultato);
  }

  @Override
  public String toString() {
    return "StrategiaRestoAlternata";
  }
}
