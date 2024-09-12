import React from 'react';
import { Link } from 'react-router-dom';

function HomePage() {
    return (
        <div className="container">
            <h1>Welcome to the Library</h1>
            <div className="button-group">
                <Link to="/books" className="button">Books</Link>
                <Link to="/authors" className="button">Authors</Link>
                <Link to="/genres" className="button">Genres</Link>
            </div>
        </div>
    );
}

export default HomePage;