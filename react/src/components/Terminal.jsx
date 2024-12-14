import React, { useState, useRef, useEffect } from 'react';

import './Hamburger.css';

const Terminal = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [history, setHistory] = useState([{ text: 'Welcome to Linux Labyrinth! Type "help" to begin...', isCommand: false }]);
  const [currentCommand, setCurrentCommand] = useState('');
  const [apiKey, setApiKey] = useState('');
  const terminalRef = useRef(null);

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  

  const handleCommand = async (e) => {
    if (e.key === 'Enter') {
      e.preventDefault();

      if( currentCommand === "clear" ){
        setHistory([{ text: '', isCommand: false }]);
        setCurrentCommand('');
      }
      
      else{
        // add command to history
        setHistory(prev => [...prev, { text: currentCommand, isCommand: true }]);
        
        // for calling backend API
        try {
          const response = await fetch('/api/execute-command', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ command: currentCommand })
          });

          // after we send post we can just set current command to nothing 
          setCurrentCommand('');

          const result = await response.json();

          // converting back to newline characters for rendering
          result.message = result.message.replace(/\\n/g, '\n');
	  console.log(apiKey);
          // adding response to history
          setHistory(prev => [...prev, { text: result.message, isCommand: false }]);
        } catch (error) {
          setHistory(prev => [...prev, { text: 'Error executing command...', isCommand: false }]);
          console.log("Error: ", error);
        }
      }
    } 
  };

  useEffect(() => {
    if (terminalRef.current) {
      terminalRef.current.scrollTop = terminalRef.current.scrollHeight;
    }
  }, [history]);

  return (
    <div className="w-full h-screen flex justify-center items-center p-4">
      <div className="w-full max-w-4xl">
        {/* Project description section */}
        <div className="mb-4 text-gray-400 font-mono text-center">
          <p>Welcome to Linux Labyrinth! Explore the commands, have fun, and learn as you go. - Ryan Mangeno </p>
        </div>
  
        {/* terminal container */}
        <div
          className="bg-black text-green-500 p-4 rounded-lg shadow-lg font-mono"
          style={{ height: '800px', width: '100%'}} 
        >
          {/* terminal body */}
          {/* ref is needed since we want to automatically scroll when entering new*/}
          <div ref={terminalRef} className="h-full flex flex-col overflow-y-auto text-left">
            {history.map((entry, index) => (
              <div key={index} className="mb-2">
                {entry.isCommand ? (
                  <div>
                    <span className="text-blue-500">player@linuxlabyrinth</span>
                    <span className="text-white">:</span>
                    <span className="text-purple-500">~$</span>
                    <span className="ml-2">{entry.text}</span>
                  </div>
                ) : (
                  <pre style={{ whiteSpace: 'pre-wrap' }}>{entry.text}</pre>
                )}
              </div>
            ))}
  
            <div className="flex">
              <span className="text-blue-500">player@linuxlabyrinth</span>
              <span className="text-white">:</span>
              <span className="text-purple-500">~$</span>
              <input
                type="text"
                value={currentCommand}
                onChange={(e) => setCurrentCommand(e.target.value)}
                onKeyDown={handleCommand}
                className="flex-1 ml-2 bg-transparent outline-none text-green-500"
                autoFocus
		maxLength = "50"
              />
            </div>
          </div>
        </div>
     </div>
 {/* Hamburger Menu */}
      <div className="fixed top-0 left-0 p-4 z-10">
        <div className={`hamburger ${isMenuOpen ? 'open' : ''}`} onClick={toggleMenu}>
          &#9776; {/* Hamburger icon */}
        </div>

        {/* Dropdown Menu (hidden by default) */}
        {isMenuOpen && (
	    <div
            className="dropdown-menu bg-gray-800 text-white p-4 mt-2 transition-transform duration-300 ease-in-out"
            style={{
              transform: 'translateX(0)', // Ensure the dropdown moves from left to right
              opacity: isMenuOpen ? 1 : 0,  // Fade-in effect
              visibility: isMenuOpen ? 'visible' : 'hidden', // Control visibility for smoother transition
            }}
            >
            <label htmlFor="api-input" className="mb-2">Enter API Key:</label>
            <input
              id="api-input"
              type="text"
              placeholder="API Key"
              onChange={(e) => setApiKey(e.target.value)}
              className="dropdown-input border border-white p-2 mt-2 rounded-md"
            />
          </div>
        )}
      </div>
    </div>
  );
  
}

export default Terminal;
