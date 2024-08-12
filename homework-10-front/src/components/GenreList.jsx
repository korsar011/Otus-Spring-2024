import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

function GenreList() {
    const [genres, setGenres] = useState([]);

    useEffect(() => {
        axios.get('/api/genres')
            .then(response => setGenres(response.data))
            .catch(error => console.error('Error fetching genres:', error));
    }, []);

    return (
        <div className="container">
            <h1>Genres List</h1>
            <table className="data-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {genres.map(genre => (
                    <tr key={genre.id}>
                        <td>{genre.id}</td>
                        <td>{genre.name}</td>
                        <td className="action-buttons">
                            <Link to={`/genres/${genre.id}`} className="edit-button">Edit</Link>
                            <button
                                onClick={() => handleDelete(genre.id)}
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
                <Link to="/genres/new" className="button">Add New Genre</Link>
                <Link to="/" className="button">Home</Link>
            </div>
        </div>
    );

    function handleDelete(id) {
        axios.delete(`/api/genres/${id}`)
            .then(() => setGenres(genres.filter(genre => genre.id !== id)))
            .catch(error => console.error('Error deleting genre:', error));
    }
}

export default GenreList;