package macchinette;

/**
 * Eccezione per rimozione impossibile da un aggregato.
 *
 * <p><strong>RI:</strong> motivo non è null.
 *
 * <p><strong>AF:</strong> rappresenta una condizione di errore in cui si tenta di rimuovere
 * monete da un aggregato ma l'operazione non può essere completata; il campo motivo specifica
 * se l'errore è dovuto a valore totale insufficiente (VALUE) o alla mancanza di monete
 * di una specifica denominazione (COINS).
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
