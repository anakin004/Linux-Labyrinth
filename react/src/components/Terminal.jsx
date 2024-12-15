import React, { useState, useRef, useEffect } from 'react';

const Terminal = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [history, setHistory] = useState([{ text: 'Welcome to Linux Labyrinth! Type "help" to begin...', isCommand: false }]);
  const [currentCommand, setCurrentCommand] = useState('');
  const [inputKey, setApiKey] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [showCopyMessage, setShowCopyMessage] = useState(false);
  const terminalRef = useRef(null);

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const handleGenerateApiKey = async () => {
    setIsLoading(true);
    try {
      const response = await fetch('/api/retrieve-key', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        }
      });

      if (!response.ok) {
        throw new Error('Failed to generate API key');
      }

      const data = await response.json();
      setApiKey(data.apiKey);
      setShowCopyMessage(true);
      setTimeout(() => setShowCopyMessage(false), 5000); // Hide message after 5 seconds
      console.log(inputKey);
    } catch (error) {
      console.error('Error generating API key:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCommand = async (e) => {
    if (e.key === 'Enter') {
      e.preventDefault();

      if (currentCommand === "clear") {
        setHistory([{ text: '', isCommand: false }]);
        setCurrentCommand('');
      } else {
        setHistory(prev => [...prev, { text: currentCommand, isCommand: true }]);

        try {
          const response = await fetch('/api/execute-command', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ command: currentCommand, apiKey: inputKey })
          });

          setCurrentCommand('');
          const result = await response.json();
          result.message = result.message.replace(/\\n/g, '\n');
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
        <div className="mb-4 text-gray-400 font-mono text-center">
          <p>Welcome to Linux Labyrinth! Explore the commands, have fun, and learn as you go. - Ryan Mangeno </p>
        </div>

        <div
          className="bg-black text-green-500 p-4 rounded-lg shadow-lg font-mono"
          style={{ height: '800px', width: '100%'}}
        >
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
                maxLength="50"
              />
            </div>
          </div>
        </div>
      </div>

      <div className="fixed top-0 left-0 p-4 z-10">
        <div className={`hamburger ${isMenuOpen ? 'open' : ''}`} onClick={toggleMenu}>
          &#9776;
        </div>

        {isMenuOpen && (
          <div
            className="dropdown-menu bg-gray-800 text-white p-4 mt-2 transition-transform duration-300 ease-in-out"
            style={{
              transform: 'translateX(0)',
              opacity: isMenuOpen ? 1 : 0,
              visibility: isMenuOpen ? 'visible' : 'hidden',
            }}
          >
            <div className="space-y-4">
              <div>
                <label htmlFor="api-input" className="block mb-2">Current API Key:</label>
                <input
                  id="api-input"
                  type="text"
                  value={inputKey}
                  onChange={(e) => setApiKey(e.target.value)}
                  className="w-full bg-gray-700 text-white border border-gray-600 p-2 rounded-md"
                  placeholder="No API Key"
                />
                {showCopyMessage && (
                  <div className="mt-2 text-green-500 text-sm">
                    API Key generated! Please copy this key now - you won't be able to see it again!
                  </div>
                )}
              </div>

              <button
                onClick={handleGenerateApiKey}
                disabled={isLoading}
                className="w-full bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded-md transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {isLoading ? 'Generating...' : 'Generate API Key (One Time!)'}
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Terminal;
