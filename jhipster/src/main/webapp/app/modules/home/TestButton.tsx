import React, { useState, useEffect } from 'react';
import axios from 'axios';

function TestButton() {
    const [data, setData] = useState(null);

    const fetchData = async () => {
        try {
            const response = await axios.get('/api/admin/users');
            setData(response.data);
            console.log(response);
        } catch (error) {
            console.error('Erreur lors de la récupération des données :', error);
        }
    };

    return (
        <div>
            <button onClick={fetchData}>Récupérer les données</button>
            {(
                <div>
                    {data && <text>Liste des utilisateurs :
                    {data.map((user) =>  <li>{user.login}</li>)}</text>}
                </div>
            )}
        </div>
    );
}

export default TestButton;