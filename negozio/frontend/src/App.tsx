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
            // Se la stella corrente è minore o uguale al valore "hover" (se ci stiamo passando sopra) 
            // oppure al valore "rating" selezionato, la coloriamo di giallo acceso, altrimenti grigio scuro.
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
  // Leggiamo i dati iniziali iniettati da Thymeleaf in reactReviews.html
  // In questo modo evitiamo una chiamata fetch() "inutile" all'avvio.
  const cdData = window.SIW_CD_DATA || { reviews: [], isLoggedIn: false };

  // Stato React per la lista delle recensioni. 
  // Inizializzato con i dati ricevuti da Thymeleaf.
  const [reviews, setReviews] = useState<Review[]>(cdData.reviews);

  // Stato React per i campi del nuovo form (Titolo, Voto, Testo)
  const [newTitle, setNewTitle] = useState('');
  const [newRating, setNewRating] = useState(5);
  const [newText, setNewText] = useState('');

  // Funzione che scatta quando clicchi "Salva Recensione"
  const handleSubmit = async (e: React.FormEvent) => {
    // Evita che la pagina si ricarichi (comportamento standard dei form HTML)
    e.preventDefault();

    try {
      // Facciamo la vera e propria chiamata REST al nostro ReviewRestController.java
      const response = await fetch(`/rest/cds/${cdData.id}/reviews`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-CSRF-TOKEN': cdData.csrfToken // Aggiungiamo il token di sicurezza CSRF per far felice Spring Boot
        },
        // Inviamo i dati come JSON
        body: JSON.stringify({
          title: newTitle,
          rating: newRating,
          text: newText
        })
      });

      if (response.ok) {
        // Se Spring Boot risponde 200 OK, ci restituisce la recensione appena salvata
        const savedReview: Review = await response.json();
        
        // Aggiorniamo lo stato di React (aggiungendo la nuova recensione in fondo)
        // Questo farà aggiornare automaticamente l'interfaccia!
        setReviews([...reviews, savedReview]);

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
    <div style={{ marginTop: '20px' }}>
      
      {/* Se l'utente è loggato, mostra il form per inserire una recensione */}
      {cdData.isLoggedIn ? (
        <div style={{ backgroundColor: '#212121', padding: '20px', borderRadius: '8px', marginBottom: '30px' }}>
          <h3>Aggiungi una Recensione (Componente React)</h3>
          
          <form onSubmit={handleSubmit}>
            <div style={{ marginBottom: '15px' }}>
              <label style={{ display: 'block', marginBottom: '5px' }}>Titolo:</label>
              {/* Quando scrivi nell'input, aggiorniamo lo stato setNewTitle */}
              <input 
                type="text" 
                value={newTitle} 
                onChange={(e) => setNewTitle(e.target.value)} 
                required 
                style={{ width: '100%', padding: '8px', borderRadius: '4px' }}
              />
            </div>

            <div style={{ marginBottom: '15px' }}>
              <label style={{ display: 'block', marginBottom: '5px' }}>Voto:</label>
              {/* Sostituiamo il brutto input numerico con il nostro componente a stelle! */}
              <StarRating rating={newRating} setRating={setNewRating} />
            </div>

            <div style={{ marginBottom: '15px' }}>
              <label style={{ display: 'block', marginBottom: '5px' }}>Testo:</label>
              <textarea 
                rows={4} 
                value={newText} 
                onChange={(e) => setNewText(e.target.value)} 
                required 
                style={{ width: '100%', padding: '8px', borderRadius: '4px' }}
              />
            </div>

            <button type="submit" style={{ padding: '10px 15px', backgroundColor: '#bb86fc', color: '#000', border: 'none', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold' }}>
              Salva Recensione
            </button>
          </form>
        </div>
      ) : (
        <div style={{ marginBottom: '20px' }}>
          <p><a href="/login" style={{ color: '#bb86fc' }}>Accedi</a> per aggiungere una recensione.</p>
        </div>
      )}

      {/* Lista delle Recensioni (Renderizzata in tempo reale) */}
      <h3>Tutte le Recensioni</h3>
      
      {reviews.length === 0 ? (
        <p>Nessuna recensione presente per questo CD.</p>
      ) : (
        // Iteriamo l'array 'reviews' e per ognuna creiamo un div
        reviews.map((r, index) => (
          <div key={index} style={{ backgroundColor: '#1e1e1e', padding: '15px', marginBottom: '15px', borderLeft: '4px solid #03dac6', borderRadius: '4px' }}>
            <h4 style={{ marginTop: 0, marginBottom: '5px' }}>
              {r.title} <span style={{ color: '#ffeb3b' }}>({r.rating}/5)</span>
            </h4>
            <div style={{ fontSize: '0.9em', color: '#aaaaaa', marginBottom: '10px' }}>
              Di: {r.author?.username || 'Utente'}
            </div>
            <p style={{ margin: 0 }}>{r.text}</p>
          </div>
        ))
      )}

    </div>
  );
}

export default App;
