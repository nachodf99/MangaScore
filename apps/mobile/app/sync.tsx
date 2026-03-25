import { useSyncStore } from '@/stores/sync.store';
import { Button, Text, View } from 'react-native';

export default function SyncScreen() {
  const { pendingCount, lastSyncAt, simulateSync } = useSyncStore();

  return (
    <View style={{ flex: 1, padding: 20, gap: 12 }}>
      <Text style={{ fontSize: 22, fontWeight: '700' }}>Cola de sincronización</Text>
      <Text>Pendientes: {pendingCount}</Text>
      <Text>Última sync: {lastSyncAt ?? 'Nunca'}</Text>
      <Button title="Sincronizar ahora" onPress={simulateSync} />
    </View>
  );
}
