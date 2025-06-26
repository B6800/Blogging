import React, {StrictMode} from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import '/src/App.css';
//Render the App component inside the root DOM node, wrapped in StrictMode for highlighting potential problem
ReactDOM.createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
/**
 * Entry point for the React application (university project).
 *
 * - Renders the main App component into the root DOM node.
 * - Wraps the App with React.StrictMode to help detect potential problems during development.
 * - Imports global styles from App.css.
 *
 * Reference:
 *   - React StrictMode: https://react.dev/reference/react/StrictMode
 */