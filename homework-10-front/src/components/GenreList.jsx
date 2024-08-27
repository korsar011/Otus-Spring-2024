import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getGenres, deleteGenre } from '../services/genreService';

function GenreList() {
    const [genres, setGenres] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        getGenres()
            .then(response => setGenres(response.data))
            .catch(error => {
                setError('Error fetching genres.');
                console.error('Error fetching genres:', error);
            });
    }, []);

    const handleDelete = (id) => {
        if (window.confirm('Are you sure you want to delete this genre?')) {
            deleteGenre(id)
                .then(() => setGenres(genres.filter(genre => genre.id !== id)))
                .catch(error => console.error('Error deleting genre:', error));
        }
    };

    if (error) {
        return <div className="container"><p>{error}</p></div>;
    }

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
}

export default GenreList;