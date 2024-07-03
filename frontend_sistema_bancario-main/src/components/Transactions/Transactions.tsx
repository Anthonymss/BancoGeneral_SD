import React, { useState } from 'react';
import { Container, Typography, Box, TextField, Button, Grid, MenuItem, Snackbar, Alert } from '@mui/material';
import SendIcon from '@mui/icons-material/Send';
import axios from 'axios';

const Transactions: React.FC = () => {
  const activeUser = localStorage.getItem('activeUser');
  const [accountNumber, setAccountNumber] = useState('');
  const [amount, setAmount] = useState(0);
  const [transactionType, setTransactionType] = useState('');
  const [notification, setNotification] = useState<{ message: string; severity: 'success' | 'error' | 'info' } | null>(null);

  const handleTransaction = async () => {
    try {
      const response = await axios.post('http://localhost:8081/transacciones/operacion', {
        tipo: transactionType,
        monto: amount,
        cuentaOrigenNumero: activeUser,
        cuentaDestinoNumero: accountNumber
      });

      console.log('Transacción exitosa:', response.data);
      setNotification({ message: 'Transacción exitosa!', severity: 'success' });
    } catch (error) {
      console.error('Error en la transacción:', error);
      setNotification({ message: 'Error en la transacción. Por favor, verifica los datos ingresados.', severity: 'error' });
    }
  };

  const handleCloseNotification = () => {
    setNotification(null);
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
              label="Número de Cuenta Destino"
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
        open={!!notification}
        autoHideDuration={6000}
        onClose={handleCloseNotification}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
      >
        <Alert
          onClose={handleCloseNotification}
          severity={notification?.severity}
          sx={{ width: '100%' }}
        >
          {notification?.message}
        </Alert>
      </Snackbar>
    </Container>
  );
};

export default Transactions;
