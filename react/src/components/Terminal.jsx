import React, { useState, useRef, useEffect } from 'react';

const Terminal = () => {
  const [history, setHistory] = useState([{ text: 'Welcome to Linux Labyrinth! Type "help" to begin...', isCommand: false }]);
  const [currentCommand, setCurrentCommand] = useState('');
  const terminalRef = useRef(null);

  const handleCommand = async (e) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      
      // add command to history
      setHistory(prev => [...prev, { text: currentCommand, isCommand: true }]);
      
      // for calling backend API
      try {
        const response = await fetch('/api/game/execute-command', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ command: currentCommand })
        });
        
        const result = await response.json();
        
        // adding response to history
        setHistory(prev => [...prev, { text: result.message, isCommand: false }]);
      } catch (error) {
        setHistory(prev => [...prev, { text: 'Error executing command...', isCommand: false }]);
      }
      
      setCurrentCommand('');
    }
  };

  useEffect(() => {
    if (terminalRef.current) {
      terminalRef.current.scrollTop = terminalRef.current.scrollHeight;
    }
  }, [history]);

  return (
    <div className="w-full max-w-4xl mx-auto p-4">
      <div 
        className="bg-black text-green-500 p-4 rounded-lg shadow-lg font-mono"
        style={{ minHeight: '400px' }}
      >
        <div 
          ref={terminalRef}
          className="h-96 overflow-y-auto"
        >
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
                <div>{entry.text}</div>
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
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Terminal;