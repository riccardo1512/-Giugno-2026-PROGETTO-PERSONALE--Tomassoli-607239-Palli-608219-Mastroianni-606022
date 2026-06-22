import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.tsx'

// Troviamo il div contenitore dentro reactReviews.html
const rootElement = document.getElementById('react-reviews-root')

if (rootElement) {
  ReactDOM.createRoot(rootElement).render(
    <React.StrictMode>
      <App />
    </React.StrictMode>,
  )
}
