import { useState } from 'react';

// Tipizzazione dei dati ricevuti da Spring Boot (Thymeleaf)
interface Review {
  id: number;
  title: string;
  text: string;
  rating: number;
  author: {
    username: string;
  };
}

interface WindowData {
  id: number;
  name: string;
  reviews: Review[];
  isLoggedIn: boolean;
  csrfToken: string;
  currentUserUsername: string;
  isAdmin: boolean;
}

// Dichiariamo che l'oggetto window ha questa nostra variabile globale
declare global {
  interface Window {
    SIW_CD_DATA: WindowData;
  }
}

// Componente React per le stelline interattive!
function StarRating({ rating, setRating }: { rating: number, setRating: (r: number) => void }) {
  // Stato interno per tenere traccia di dove si trova il mouse quando facciamo "hover"
  const [hover, setHover] = useState(0);

  return (
    <div style={{ display: 'flex', gap: '5px' }}>
      {[1, 2, 3, 4, 5].map((star) => (
        <span
          key={star}
          style={{
            cursor: 'pointer',
            fontSize: '28px',
            color: star <= (hover || rating) ? '#ffeb3b' : '#444',
            transition: 'color 0.2s'
          }}
          onClick={() => setRating(star)}
          onMouseEnter={() => setHover(star)}
          onMouseLeave={() => setHover(0)}
        >
          ★
        </span>
      ))}
    </div>
  );
}

function App() {
  const cdData = window.SIW_CD_DATA || { id: 0, name: '', reviews: [], isLoggedIn: false, csrfToken: '', currentUserUsername: '', isAdmin: false };

  // Stato React per i campi del nuovo form (Titolo, Voto, Testo)
  const [newTitle, setNewTitle] = useState('');
  const [newRating, setNewRating] = useState(5);
  const [newText, setNewText] = useState('');

  // Funzione che scatta quando clicchi "Salva Recensione"
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch(`/rest/cds/${cdData.id}/reviews`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-CSRF-TOKEN': cdData.csrfToken
        },
        body: JSON.stringify({
          title: newTitle,
          rating: newRating,
          text: newText
        })
      });

      if (response.ok) {
        alert("Recensione inserita con successo! Torna alla pagina del CD per visualizzarla.");
        
        // Svuotiamo i campi del form
        setNewTitle('');
        setNewText('');
        setNewRating(5);
      } else {
        alert("Errore durante il salvataggio della recensione.");
      }
    } catch (error) {
      console.error(error);
      alert("Errore di rete.");
    }
  };

  return (
    <div className="reviews-wrapper">
      
      {cdData.isLoggedIn ? (
        <div className="review-form-container glass-panel">
          <h3 className="section-title">Aggiungi una Recensione</h3>
          
          <form onSubmit={handleSubmit} className="review-form">
            <div className="form-group">
              <label>Titolo:</label>
              <input 
                type="text" 
                value={newTitle} 
                onChange={(e) => setNewTitle(e.target.value)} 
                required 
                className="form-input"
                placeholder="Un titolo accattivante..."
              />
            </div>
            
            <div className="form-group rating-group">
              <label>Voto:</label>
              <StarRating rating={newRating} setRating={setNewRating} />
            </div>

            <div className="form-group">
              <label>Testo:</label>
              <textarea 
                rows={4} 
                value={newText} 
                onChange={(e) => setNewText(e.target.value)} 
                required 
                className="form-input"
                placeholder="Scrivi qui la tua opinione..."
              />
            </div>

            <button type="submit" className="btn-submit ripple">
              Salva Recensione
            </button>
          </form>
        </div>
      ) : (
        <div className="login-prompt glass-panel">
          <p><a href="/login" className="accent-link">Accedi</a> per aggiungere una recensione.</p>
        </div>
      )}

    </div>
  );
}

export default App;
