import React, { useState } from 'react';
import { Container, Typography, Box, TextField, Button, Grid, MenuItem, Snackbar } from '@mui/material';
import MonetizationOnIcon from '@mui/icons-material/MonetizationOn';
import SendIcon from '@mui/icons-material/Send';
import axios from 'axios';

const Transactions: React.FC = () => {
  const activeUser = localStorage.getItem('activeUser');
  const [accountNumber, setAccountNumber] = useState('');
  const [amount, setAmount] = useState(0);
  const [description, setDescription] = useState('');
  const [transactionType, setTransactionType] = useState('');
  const [error, setError] = useState('');
  const [openError, setOpenError] = useState(false);
  const [openSuccess, setOpenSuccess] = useState(false);

  const handleTransaction = async () => {
    try {
      const response = await axios.post('http://localhost:8081/transacciones/operacion', {
        tipo: transactionType,
        monto: amount,
        cuentaOrigenNumero: activeUser, // Utiliza el número de cuenta del usuario activo
        cuentaDestinoNumero: accountNumber // Cuenta destino ingresada por el usuario
      });

      console.log('Transacción exitosa:', response.data);
      setOpenSuccess(true);
      setOpenError(false);
    } catch (error) {
      console.error('Error en la transacción:', error);
      setError('Error en la transacción. Por favor, verifica los datos ingresados.');
      setOpenError(true);
      setOpenSuccess(false);
    }
  };

  const handleCloseError = () => {
    setOpenError(false);
  };

  const handleCloseSuccess = () => {
    setOpenSuccess(false);
  };

  return (
    <Container>
      <Typography variant="h4" gutterBottom>
        Transacciones
      </Typography>
      <Box component="form" sx={{ mt: 3 }} noValidate autoComplete="off">
        <Grid container spacing={3}>
          <Grid item xs={12} sm={6}>
            <TextField
              required
              fullWidth
              id="account-number"
              label="Número de Cuenta"
              value={accountNumber}
              onChange={(e) => setAccountNumber(e.target.value)}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              required
              fullWidth
              id="amount"
              label="Monto"
              type="number"
              value={amount}
              onChange={(e) => setAmount(Number(e.target.value))}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              required
              fullWidth
              id="description"
              label="Descripción"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              select
              fullWidth
              id="transaction-type"
              label="Tipo de Transacción"
              value={transactionType}
              onChange={(e) => setTransactionType(e.target.value)}
            >
              <MenuItem value="DEPOSITO">Depósito</MenuItem>
              <MenuItem value="RETIRO">Retiro</MenuItem>
              <MenuItem value="TRANSFERENCIA">Transferencia</MenuItem>
            </TextField>
          </Grid>
        </Grid>
        <Button
          type="submit"
          variant="contained"
          color="primary"
          startIcon={<SendIcon />}
          sx={{ mt: 3, mb: 2 }}
          onClick={handleTransaction}
        >
          Realizar Transacción
        </Button>
      </Box>
      <Snackbar
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
        open={openSuccess}
        autoHideDuration={6000}
        onClose={handleCloseSuccess}
        message="Transacción exitosa!"
        sx={{ backgroundColor: '#4caf50' }}
      />
      <Snackbar
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
        open={openError}
        autoHideDuration={6000}
        onClose={handleCloseError}
        message={error}
        sx={{ backgroundColor: '#f44336' }}
      />
    </Container>
  );
};

export default Transactions;
