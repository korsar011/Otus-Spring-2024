import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getAuthors, deleteAuthor } from '../services/authorService';

function AuthorList() {
    const [authors, setAuthors] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        getAuthors()
            .then(response => setAuthors(response.data))
            .catch(error => {
                setError('Error fetching authors.');
                console.error('Error fetching authors:', error);
            });
    }, []);

    function handleDelete(id) {
        if (window.confirm('Are you sure you want to delete this author?')) {
            deleteAuthor(id)
                .then(() => {
                    setAuthors(authors.filter(author => author.id !== id));
                    setError(null); // Сброс ошибки при успешном удалении
                })
                .catch(error => {
                    setError(error.message || 'Error deleting author.'); // Установка сообщения об ошибке
                    console.error('Error deleting author:', error);
                });
        }
    }

    return (
        <div className="container">
            <h1>Authors List</h1>
            {error && <div className="error-message">{error}</div>} {/* Отображение сообщения об ошибке */}
            <table className="data-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Full Name</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {authors.length > 0 ? (
                    authors.map(author => (
                        <tr key={author.id}>
                            <td>{author.id}</td>
                            <td>{author.fullName}</td>
                            <td className="action-buttons">
                                <Link to={`/authors/${author.id}`} className="edit-button">Edit</Link>
                                <button
                                    onClick={() => handleDelete(author.id)}
                                    className="delete-button"
                                >
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="3">No authors available.</td>
                    </tr>
                )}
                </tbody>
            </table>
            <div className="button-group">
                <Link to="/authors/new" className="button">Add New Author</Link>
                <Link to="/" className="button">Home</Link>
            </div>
        </div>
    );
}

export default AuthorList;