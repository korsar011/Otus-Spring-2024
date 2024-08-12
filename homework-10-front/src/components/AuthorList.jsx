import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

function AuthorList() {
    const [authors, setAuthors] = useState([]);

    useEffect(() => {
        axios.get('/api/authors')
            .then(response => setAuthors(response.data))
            .catch(error => console.error('Error fetching authors:', error));
    }, []);

    return (
        <div className="container">
            <h1>Authors List</h1>
            <table className="data-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Full Name</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {authors.map(author => (
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
                ))}
                </tbody>
            </table>
            <div className="button-group">
                <Link to="/authors/new" className="button">Add New Author</Link>
                <Link to="/" className="button">Home</Link>
            </div>
        </div>
    );

    function handleDelete(id) {
        axios.delete(`/api/authors/${id}`)
            .then(() => setAuthors(authors.filter(author => author.id !== id)))
            .catch(error => console.error('Error deleting author:', error));
    }
}

export default AuthorList;