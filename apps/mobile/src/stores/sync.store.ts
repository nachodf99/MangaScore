import { create } from 'zustand';

type SyncState = {
  pendingCount: number;
  lastSyncAt: string | null;
  simulateSync: () => void;
};

export const useSyncStore = create<SyncState>((set) => ({
  pendingCount: 3,
  lastSyncAt: null,
  simulateSync: () => set({ pendingCount: 0, lastSyncAt: new Date().toISOString() })
}));
