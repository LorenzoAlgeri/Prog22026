package macchinette;

/**
 * Eccezione per rimozione impossibile da un aggregato.
 *
 * <p>Motivi: {@link Motivo#VALUE} se valore insufficiente,
 * {@link Motivo#COINS} se mancano monete del taglio richiesto.
 */
public class AggregatoInsufficienteException extends Exception {

  private static final long serialVersionUID = 1L;

  /** Motivi dell'insufficienza. */
  public enum Motivo {
    /** Valore totale insufficiente. */
    VALUE,
    /** Monete del taglio richiesto insufficienti. */
    COINS
  }

  private final Motivo motivo;

  /**
   * Crea l'eccezione con il motivo specificato.
   *
   * @param motivo causa dell'errore
   */
  public AggregatoInsufficienteException(Motivo motivo) {
    super(getMessaggio(motivo));
    this.motivo = motivo;
  }

  private static String getMessaggio(Motivo m) {
    if (m == Motivo.VALUE) {
      return "value";
    } else {
      return "coins";
    }
  }

  /** Restituisce il motivo. */
  public Motivo motivo() {
    return motivo;
  }
}
